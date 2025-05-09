package com.foodu.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "truck_menu")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    private String menuName;
    private String menuPrice;
    private String menuImage;
}