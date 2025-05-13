package com.foodu.repository;

import com.foodu.entity.Event;
import com.foodu.entity.Truck;
import com.foodu.entity.User;
import com.foodu.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    // 회원: 하루 3회 제한
    int countByUserAndEventAndVotedAtBetween(User user, Event event, String start, String end);

    // 비회원: fingerprint 기준 하루 3회 제한
    int countByFingerprintAndEventAndVotedAtBetween(String fingerprint, Event event, String start, String end);

    Optional<Vote> findByUserAndEventAndTruck(User user, Event event, Truck truck);
}
