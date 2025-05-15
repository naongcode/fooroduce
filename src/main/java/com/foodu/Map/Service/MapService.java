package com.foodu.Map.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodu.Map.Dto.EventMarkerResponse;
import com.foodu.Map.Dto.GeocodeResponse;
import com.foodu.entity.Event;
import com.foodu.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapService {

    private final EventRepository eventRepository;

    @Value("${kakao.api-key}")
    private String kakaoApiKey;

    public GeocodeResponse geocodeAddress(String address) {
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + address;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode documents = mapper.readTree(response.getBody()).get("documents");
            if (documents.isArray() && documents.size() > 0) {
                JsonNode first = documents.get(0);
                double lat = first.get("y").asDouble();
                double lng = first.get("x").asDouble();
                return new GeocodeResponse(lat, lng);
            }
        } catch (Exception e) {
            throw new RuntimeException("Geocoding failed", e);
        }

        throw new RuntimeException("No result found");
    }

    public List<EventMarkerResponse> getEventMarkers(String region) {
        List<Event> events = (region != null && !region.isEmpty())
                ? eventRepository.findByLocationContaining(region)
                : eventRepository.findAll();

        return events.stream()
                .map(e -> new EventMarkerResponse(
                        e.getEventId(),
                        e.getEventName(),
                        e.getLatitude(),
                        e.getLongitude()
                )).collect(Collectors.toList());
    }
}
