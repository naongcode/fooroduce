package com.foodu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vote_result")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteResultId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    private Integer voteCount;
    private String savedAt;
}
