package com.foodu.Event.Controller;

import com.foodu.Event.Dto.ApplicationStatusUpdateRequest;
import com.foodu.Event.Service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PatchMapping("/{eventId}")
    public ResponseEntity<?> updateApplicationStatuses(
            @PathVariable Integer eventId,
            @RequestBody List<ApplicationStatusUpdateRequest> requestList
    ) {
        try {
            applicationService.updateApplicationStatuses(eventId, requestList);
            return ResponseEntity.ok(Collections.singletonMap("message", "success"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

}
