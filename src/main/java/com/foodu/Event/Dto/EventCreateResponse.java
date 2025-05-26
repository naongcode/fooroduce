package com.foodu.Event.Dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
public class EventCreateResponse {
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
