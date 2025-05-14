package com.foodu.Vote;

import com.foodu.Vote.Dto.VoteRequest;
import com.foodu.util.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.foodu.entity.VoteResult;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/api/votes")
    public ResponseEntity<?> vote(@RequestBody VoteRequest dto,
                                  @RequestHeader(value = "Authorization", required = false) String token,
                                  @RequestHeader(value = "fingerprint", required = false) String fingerprint) {
        try {
            voteService.vote(dto, token, fingerprint);  // 토큰을 서비스에 전달, 핑거프린트도 전달
            return ResponseEntity.ok("투표 성공");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("서버 오류: " + e.getMessage());
        }
    }

    // 엔드포인트 event -> events로 변경
    @GetMapping("/api/events/{eventId}/votes")
    public ResponseEntity<?> getVoteResults(@PathVariable Integer eventId,
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token == null || !token.startsWith("Bearer ") ||
                    !JwtTokenProvider.isTokenValid(token.substring(7))) {
                return ResponseEntity.status(403).body("회원만 조회할 수 있습니다.");
            }

            // 실제 결과 가져오기
            List<VoteResult> results = voteService.getVoteResults(eventId);
            return ResponseEntity.ok(results);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("투표 중 서버 오류 발생", e); // ← 로그 파일로 기록
            return ResponseEntity.internalServerError().body("서버 오류: " + e.getMessage());
        }
    }

}
