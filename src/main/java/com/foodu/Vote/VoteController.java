package com.foodu.Vote;

import com.foodu.Vote.Dto.VoteRequest;
import com.foodu.Vote.Dto.VoteResponse;
import com.foodu.Vote.Dto.VoteStatusResponse;
import com.foodu.util.ExtractInfoFromToken;
import com.foodu.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.foodu.entity.VoteResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            // 여기서 dto에 헤더로 받은 fingerprint 세팅
            dto.setFingerprint(fingerprint);

            voteService.vote(dto, token, fingerprint);  // 토큰을 서비스에 전달, 핑거프린트도 전달
            return ResponseEntity.ok("투표 성공");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("투표 중 서버 오류 발생", e); // ← 로그 파일로 기록
            return ResponseEntity.internalServerError().body("서버 오류: " + e.getMessage());
        }
    }

    // 투표 결과
    @GetMapping("/api/votes/results/{eventId}")
    public ResponseEntity<?> getVoteResults(@PathVariable Integer eventId,
                                            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
//            if (token == null || !token.startsWith("Bearer ") ||
//                    !JwtTokenProvider.isTokenValid(token.substring(7))) {
//                return ResponseEntity.status(403).body("회원만 조회할 수 있습니다.");
//            }

            // 실제 결과 가져오기
            List<VoteResponse> results = voteService.getVoteResponses(eventId);
            return ResponseEntity.ok(results);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("투표 중 서버 오류 발생", e); // ← 로그 파일로 기록
            return ResponseEntity.internalServerError().body("서버 오류: " + e.getMessage());
        }
    }

    @GetMapping("/api/votes/status/{eventId}")
    public ResponseEntity<List<VoteStatusResponse>> getVoteStatus(
            @PathVariable Integer eventId,
            @RequestParam(required = false) String fingerprint,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String userId = null;

        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            if (JwtTokenProvider.isTokenValid(jwt)) {
                userId = ExtractInfoFromToken.getUserIdFromToken(jwt);
            }
        }

        List<VoteStatusResponse> responses = voteService.checkVoteStatus(eventId, userId, fingerprint);

        return ResponseEntity.ok(responses);
    }

}
