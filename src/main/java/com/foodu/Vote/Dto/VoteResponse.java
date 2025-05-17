package com.foodu.Vote.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {
    private Integer truckId;
    private String truckName;
    private String menuImage;
    private Integer voteCount;
}