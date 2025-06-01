package com.foodu.Event.Dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import com.foodu.entity.TruckApplication;

@Getter @Setter
public class ApplicationStatusUpdateRequest {

    private Integer applicationId;
    private TruckApplication.Status status;  // "ACCEPTED" or "REJECTED"
}
