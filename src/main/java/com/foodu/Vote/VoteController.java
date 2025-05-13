package com.foodu.Vote;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodu.entity.VoteResult;

import java.util.List;


@RestController
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/api/votes") //경로 수정
    public ResponseEntity<?> vote(@RequestBody VoteRequestDto dto) {
        try {
            voteService.vote(dto);
            return ResponseEntity.ok("투표가 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류: " + e.getMessage());
        }
    }

    @GetMapping("/api/event/{eventId}/votes") //엔드포인트 잘못 설정됨 수정함. results/{eventId} -> {eventId}/votes
    public ResponseEntity<?> getVoteResults(@PathVariable Integer eventId) {
        try {
            List<VoteResult> results = voteService.getVoteResults(eventId);
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류: " + e.getMessage());
        }
    }

}
