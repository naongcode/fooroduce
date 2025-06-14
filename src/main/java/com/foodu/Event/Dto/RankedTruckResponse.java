package com.foodu.Event.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RankedTruckResponse {
    private Integer truckId;
    private String truckName;
    private String description;
    private String phoneNumber;
    private String status;
    private Integer applicationId;
    private List<Menu> menus;
    private Integer voteCount;

    @Getter
    @Builder
    public static class Menu {
        private String menuName;
        private String menuPrice;
        private String menuImage;
        private String menuType;
    }
}