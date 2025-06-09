package com.foodu.Truck.Controller;

import com.foodu.Truck.Dto.TruckApplicationRequest;
import com.foodu.Truck.Dto.TruckApplicationResponse;
import com.foodu.Truck.Dto.TruckResponse;
import com.foodu.Truck.Service.TruckService;
import com.foodu.entity.Truck;
import com.foodu.entity.TruckApplication;
import com.foodu.repository.TruckApplicationRepository;
import com.foodu.repository.TruckRepository;
import com.foodu.util.ExtractInfoFromToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class TruckController {

    private final TruckService truckService;
    private final TruckRepository truckRepository;

    @PostMapping
    public ResponseEntity<String> applyTruck(@RequestBody TruckApplicationRequest request, HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다.");
        }

        String token = authHeader.substring(7); // "Bearer " 이후 부분
        String userId = ExtractInfoFromToken.getUserIdFromToken(token);

        List<Truck> trucks = truckRepository.findByOwner_UserId(userId);
        if (trucks.isEmpty()) {
            throw new RuntimeException("해당 유저의 트럭이 없습니다.");
        }

        Integer truckId = trucks.get(0).getTruckId(); // 첫 번째 트럭 사용

        truckService.applyToEvent(request.getEventId(), truckId);
        return ResponseEntity.ok("신청 완료");
    }


    @GetMapping("/truck")
    public ResponseEntity<List<TruckResponse>> getMyTruckApplications(HttpServletRequest request) {
        String ownerId = (String) request.getAttribute("userId"); // JwtAuthFilter가 넣어준 값

        List<TruckResponse> applications = truckService.getApplicationsByOwner(ownerId);
        return ResponseEntity.ok(applications);
    }
}
