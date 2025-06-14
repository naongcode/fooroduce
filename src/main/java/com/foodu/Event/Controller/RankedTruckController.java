package com.foodu.Event.Controller;

import com.foodu.Event.Dto.RankedTruckResponse;
import com.foodu.Event.Service.RankedTruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class RankedTruckController {

    private final RankedTruckService rankedTruckService;

    // GET /api/events/{eventId}/ranked-trucks
    @GetMapping("/{eventId}/ranked-trucks")
    public List<RankedTruckResponse> getRankedTrucks(@PathVariable Integer eventId) {
        return rankedTruckService.getTop3RankedTrucks(eventId);
    }
}
