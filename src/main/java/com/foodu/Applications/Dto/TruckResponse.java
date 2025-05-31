package com.foodu.Applications.Dto;

import com.foodu.entity.Event;
import com.foodu.entity.TruckApplication;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TruckResponse {
    private Integer applicationId;
    private Integer eventId;
    private String eventName;
    private String recruitStart;
    private String recruitEnd;
    private String eventStart;
    private String eventEnd;
    private String location;
    private String status;

    public static TruckResponse from(TruckApplication app) {
        Event event = app.getEvent();

        return TruckResponse.builder()
                .eventId(event.getEventId())
                .applicationId(app.getApplicationId())
                .eventName(event.getEventName())
                .recruitStart(event.getRecruitStart().toString())
                .recruitEnd(event.getRecruitEnd().toString())
                .eventStart(event.getEventStart().toString())
                .eventEnd(event.getEventEnd().toString())
                .location(event.getLocation())
                .status(app.getStatus().name())
                .build();
    }
}