package com.foodu.Event.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRecommendResponse {
    private Integer eventId;
    private String eventName;
    private String eventHost;
    private String eventImage;
    private String description;
    private String location;
    private Double latitude;
    private Double longitude;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
}
