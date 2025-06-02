package com.foodu.Truck.Dto;

public class TruckApplicationResponse {
    private Integer applicationId;
    private Integer truckId;
    private Integer eventId;
    private String status;

    public TruckApplicationResponse(Integer applicationId, Integer truckId, Integer eventId, String status) {
        this.applicationId = applicationId;
        this.truckId = truckId;
        this.eventId = eventId;
        this.status = status;
    }
}

