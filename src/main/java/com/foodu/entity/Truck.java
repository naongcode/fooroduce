package com.foodu.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String phoneNumber;

    //@Lob
    private String description;

    private String createdAt;
}
