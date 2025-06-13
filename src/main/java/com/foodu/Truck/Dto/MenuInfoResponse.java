package com.foodu.Truck.Dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuInfoResponse {
    private final Integer menuId;
    private final String menuName;
    private final String menuPrice;
    private final String menuImage;
    @Column(name = "menu_type")
    private final String menuType;

    // menuId만 받는 생성자 추가
    public MenuInfoResponse(Integer menuId) {
        this.menuId = menuId;
        this.menuName = null;
        this.menuPrice = null;
        this.menuImage = null;
        this.menuType = null;
    }
}