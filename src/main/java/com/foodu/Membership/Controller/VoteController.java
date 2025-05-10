package com.foodu.Membership.Controller;

import com.foodu.Membership.Dto.VoteRequestDto;
import com.foodu.Membership.Service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.foodu.entity.VoteResult;

import java.util.List;


@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
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

    @GetMapping("/results/{eventId}")
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
