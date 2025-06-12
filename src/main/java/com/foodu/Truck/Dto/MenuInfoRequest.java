package com.foodu.Truck.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuInfoRequest {
    private Integer truckId;
    private String menuName;
    private String menuPrice;
    private String menuImage; // 이미지 업로드는 별도 처리하고 URL 저장
    private String menuType;
}