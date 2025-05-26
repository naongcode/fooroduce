package com.foodu.Event.Controller;

import com.foodu.Event.Dto.*;
import com.foodu.Event.Service.EventManagementService;
import com.foodu.Event.Service.EventService;
import com.foodu.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final EventManagementService eventManagementService;

    //전체 행사 목록 조회
    @GetMapping
    public List<AllEventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

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

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventDetail(@PathVariable Integer eventId) {
        EventResponse response = eventService.getEventDetail(eventId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(
            @RequestBody EventCreateRequest request,
            HttpServletRequest httpRequest) {

        String userId = (String) httpRequest.getAttribute("userId");
        String role = (String) httpRequest.getAttribute("role");

        if (!"EVENT_MANAGER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("행사 담당자만 등록 가능합니다.");
        }

        EventCreateResponse response = eventManagementService.createEvent(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



}
