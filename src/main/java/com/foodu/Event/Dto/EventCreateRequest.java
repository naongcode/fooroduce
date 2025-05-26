package com.foodu.Event.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EventCreateRequest {
    private String eventName;
    private String eventHost;
    private String eventImage;
    private String description;
    private LocalDateTime recruitStart;
    private LocalDateTime recruitEnd;
    private LocalDateTime voteStart;
    private LocalDateTime voteEnd;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private String location;
    private Integer truckCount;

}
