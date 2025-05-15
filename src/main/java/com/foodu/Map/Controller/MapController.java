package com.foodu.Map.Controller;

import com.foodu.Map.Dto.EventMarkerResponse;
import com.foodu.Map.Dto.GeocodeRequest;
import com.foodu.Map.Dto.GeocodeResponse;
import com.foodu.Map.Service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping
    public Map<String, Object> getDefaultMapLocation() {
        Map<String, Object> response = new HashMap<>();
        response.put("latitude", 37.5668);
        response.put("longitude", 126.9786);
        response.put("zoom_level", 3);
        response.put("message", "지도 기본 로딩 성공");
        return response;
    }

    @PostMapping("/geocode")
    public GeocodeResponse geocode(@RequestBody GeocodeRequest request) {
        return mapService.geocodeAddress(request.getAddress());
    }

    @GetMapping("/events")
    public List<EventMarkerResponse> getEventMarkers(@RequestParam(required = false) String region) {
        return mapService.getEventMarkers(region);
    }
}
