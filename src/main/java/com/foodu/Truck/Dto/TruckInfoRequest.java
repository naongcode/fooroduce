package com.foodu.Truck.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TruckInfoRequest {
    private Integer truckId; // 수정 시 사용
    private String name;
    private String phoneNumber;
    private String description;
    private String menuType;
    private Integer ownerId; // 사용자 식별자
}