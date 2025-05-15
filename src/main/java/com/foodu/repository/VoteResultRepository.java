package com.foodu.repository;

import com.foodu.entity.Event;
import com.foodu.entity.Truck;
import com.foodu.entity.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteResultRepository extends JpaRepository<VoteResult, Integer> {
    Optional<VoteResult> findByEventAndTruck(Event event, Truck truck);
    List<VoteResult> findAllByEventOrderByVoteCountDesc(Event event);
}
