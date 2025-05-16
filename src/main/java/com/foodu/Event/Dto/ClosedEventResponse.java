package com.foodu.Event.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class ClosedEventResponse {
    @Column(name = "event_id")
    private Integer eventId;
    @Column(name = "event_name")
    private String eventName;
    @Column(name = "event_host")
    private String eventHost;
    @Column(name = "event_image")
    private String eventImage;

    @Column(name = "event_start")
    private LocalDateTime eventStart;
    @Column(name = "event_end")
    private LocalDateTime eventEnd;

    @Column(name = "recruit_start")
    private LocalDateTime recruitStart;

    @Column(name = "recruit_end")
    private LocalDateTime recruitEnd;

    @Column(name = "vote_start")
    private LocalDateTime voteStart;
    @Column(name = "vote_end")
    private LocalDateTime voteEnd;
}

