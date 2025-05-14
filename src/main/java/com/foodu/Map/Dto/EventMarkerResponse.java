package com.foodu.Map.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventMarkerResponse {
    private Integer eventId;
    private String eventName;
    private Double latitude;
    private Double longitude;
}