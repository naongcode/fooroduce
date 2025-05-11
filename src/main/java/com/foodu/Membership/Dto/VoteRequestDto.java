package com.foodu.Membership.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteRequestDto {
    private String userId;
    private Integer  truckId;
    private Integer  eventId;
    private String fingerprint;
}
