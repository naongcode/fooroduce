package com.foodu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

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

    private LocalDateTime appliedAt;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "fooroduce.application_status") // DB enum 타입 명시
    private Status status;

    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }
}
