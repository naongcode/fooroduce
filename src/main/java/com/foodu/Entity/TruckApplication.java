package com.foodu.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "truck_application")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer applicationId;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private String appliedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }
}