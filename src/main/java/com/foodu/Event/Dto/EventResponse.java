package com.foodu.Event.Dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    // Event안에 Truck정보 있고, 다시 그안에 Menu정보 있음.
    // === Event 기본 정보 ===
    private Integer eventId;
    private String eventName;
    private String eventHost;
    private String eventImage;

    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;

    private String description;
    private Integer truckCount;

    private LocalDateTime recruitStart;
    private LocalDateTime recruitEnd;

    private LocalDateTime voteStart;
    private LocalDateTime voteEnd;

    private String location;
    private Double latitude;
    private Double longitude;

    private String createdBy;
    private LocalDateTime createdAt;

    private int currentPage;
    private int totalPages;
    private long totalElements;

    // === 트럭 + 메뉴 정보 통합 ===
    private List<TruckWithMenu> trucks;

    @Data
    @Builder
    public static class TruckWithMenu {
        private Integer truckId;
        private String truckName;
        private String description;
        private String phoneNumber;
        private String status; // 추가: 승인 상태
        private Integer applicationId;
        private List<Menu> menus;

        @Data
        @Builder
        public static class Menu {
            private String menuName;
            private String menuPrice;
            private String menuImage;
            private String menuType;
        }
    }

//  구조
//    {
//  "eventId": 1,
//  "eventName": "봄꽃축제",
//  "trucks": [
//    {
//      "truckId": 101,
//      "truckName": "맛있는차차",
//      "description": "수제버거 전문 푸드트럭입니다.",
//      "menus": [
//        {
//          "menuName": "치즈버거",
//          "menuPrice": 7000,
//          "menuImage": "https://..."
//          "menuType" : "양식"
//        }
//      ]
//    }
//  ]
//}

}