package com.foodu.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;


@Entity
@Table(name = "event")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;

    @Column(name = "event_name")
    private String eventName;
    @Column(name = "event_host")
    private String eventHost;
    @Column(name = "event_image")
    private String eventImage;

    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;

    //@Lob
    @Column(name = "description", columnDefinition = "TEXT") // columnDefinition 사용
    private String description;

    @Column(name = "truck_count")
    private Integer truckCount;


    @Column(name = "recruit_start")
    private LocalDateTime recruitStart;

    @Column(name = "recruit_end")
    private LocalDateTime recruitEnd;

    @Column(name = "vote_start")
    private LocalDateTime voteStart;
    @Column(name = "vote_end")
    private LocalDateTime voteEnd;

    @Column(name = "location")
    private String location;

    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_at")
    private String createdAt;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(name = "location_point", columnDefinition = "geometry(Point,4326)")
    private Point locationPoint;
}
