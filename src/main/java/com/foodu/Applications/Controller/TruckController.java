package com.foodu.Applications.Controller;

import com.foodu.Applications.Dto.TruckResponse;
import com.foodu.Applications.Service.TruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/applications/truck")
@RequiredArgsConstructor
public class TruckController {

    private final TruckService truckService;

    @GetMapping
    public ResponseEntity<List<TruckResponse>> getMyTruckApplications(HttpServletRequest request) {
        String ownerId = (String) request.getAttribute("userId"); // JwtAuthFilter가 넣어준 값

        List<TruckResponse> applications = truckService.getApplicationsByOwner(ownerId);
        return ResponseEntity.ok(applications);
    }
}
