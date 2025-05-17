package com.foodu.Vote.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteStatusResponse {
    private Integer truckId;
    private boolean alreadyVoted;
}
