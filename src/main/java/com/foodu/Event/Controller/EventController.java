package com.foodu.Event.Controller;

import com.foodu.Event.Dto.ClosedEventResponse;
import com.foodu.Event.Dto.OngoingEventResponse;
import com.foodu.Event.Service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    // 현재 투표 가능한 행사 목록 조회
    @GetMapping("/ongoing")
    public ResponseEntity<List<OngoingEventResponse>> getOngoingEvents() {
        List<OngoingEventResponse> events = eventService.getOngoingEvents();
        return ResponseEntity.ok(events);
    }

    // 종료된 행사 목록 조회
    @GetMapping("/closed")
    public ResponseEntity<List<ClosedEventResponse>> getClosedEvents() {
        List<ClosedEventResponse> events = eventService.getClosedEvents();
        return ResponseEntity.ok(events);
    }


}
