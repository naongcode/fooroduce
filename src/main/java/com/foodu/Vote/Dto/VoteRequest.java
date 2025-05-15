package com.foodu.Vote.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoteRequest {

    @Column(name = "truck_id")
    private Integer  truckId;

    @Column(name = "event_id")
    private Integer  eventId;

    private String fingerprint;

    private String token;

}
