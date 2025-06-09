package com.foodu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "truck")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer truckId;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    //@Lob
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "truck")
    private List<TruckMenu> truckMenus;
}
