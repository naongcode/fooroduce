package com.foodu.repository;

import com.foodu.entity.Event;
import com.foodu.entity.Truck;
import com.foodu.entity.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteResultRepository extends JpaRepository<VoteResult, Integer> {
    Optional<VoteResult> findByEventAndTruck(Event event, Truck truck);
    List<VoteResult> findAllByEventOrderByVoteCountDesc(Event event);

    @Query("SELECT vr.truck.truckId, SUM(vr.voteCount) FROM VoteResult vr WHERE vr.event.eventId = :eventId GROUP BY vr.truck.truckId")
    List<Object[]> findVoteCountsByEventId(@Param("eventId") Integer eventId);
}
