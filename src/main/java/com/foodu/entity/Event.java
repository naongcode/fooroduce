package com.foodu.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String eventName;
    private String eventHost;
    private String eventImage;

    private String eventStart;
    private String eventEnd;

    @Lob
    private String description;

    private Integer truckCount;

    private String recruitStart;
    private String recruitEnd;
    private String voteStart;
    private String voteEnd;

    private String location;

    private Double latitude;
    private Double longitude;

    private Integer createdBy;
    private String createdAt;
}
