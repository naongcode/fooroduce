package com.foodu.Truck.Dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TruckWithMenusResponse {
    private Integer truckId;
    private String name;
    private String phoneNumber;
    private String description;
    private List<MenuInfoResponse> menus;
}
