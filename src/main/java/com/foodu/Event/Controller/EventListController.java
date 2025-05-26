package com.foodu.Event.Controller;

import com.foodu.Event.Dto.EventResponse;
import com.foodu.Event.Service.EventListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/list")
@RequiredArgsConstructor
public class EventListController {

    private final EventListService eventListService;

    // 로그인된 행사담당자가 등록한 행사 정보 조회
    @GetMapping
    public ResponseEntity<List<EventResponse>> getMyEvents(
            @RequestAttribute("userId") String userId
    ) {
        List<EventResponse> events = eventListService.getEventsByManager(userId);
        return ResponseEntity.ok(events);
    }
}
