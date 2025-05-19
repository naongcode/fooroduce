package com.foodu.Event.Controller;

import com.foodu.Event.Dto.EventRecommendResponse;
import com.foodu.Event.Service.EventRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/recommend")
@RequiredArgsConstructor
public class EventRecommendController {

    private final EventRecommendService eventRecommendService;

    @GetMapping("/nearby")
    public ResponseEntity<List<EventRecommendResponse>> getNearbyEvents(
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam(defaultValue = "5000") double radius) {

        List<EventRecommendResponse> nearbyEvents = eventRecommendService.findNearbyEvents(longitude, latitude, radius);

        return ResponseEntity.ok(nearbyEvents);
    }
}
