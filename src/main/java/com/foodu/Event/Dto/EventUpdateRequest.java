package com.foodu.Event.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventUpdateRequest {
    private Integer eventId;
    private String eventName;
    private String eventHost;
    private String eventImage;
    private String description;
    private String location;
    private LocalDateTime recruitStart;
    private LocalDateTime recruitEnd;
    private LocalDateTime voteStart;
    private LocalDateTime voteEnd;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private Integer truckCount;
}
