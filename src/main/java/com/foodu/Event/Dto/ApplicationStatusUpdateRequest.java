package com.foodu.Event.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApplicationStatusUpdateRequest {
    private Integer application_id;
    private String status;  // "ACCEPTED" or "REJECTED"
}
