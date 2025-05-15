package com.foodu.repository;

import com.foodu.entity.Event;
import com.foodu.entity.Truck;
import com.foodu.entity.User;
import com.foodu.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    // 회원: 하루 3회 제한
    int countByUserAndEventAndVotedAtBetween(User user, Event event, LocalDateTime start, LocalDateTime end);

    // 비회원: fingerprint 기준 하루 3회 제한
    int countByFingerprintAndEventAndVotedAtBetween(String fingerprint, Event event, LocalDateTime start, LocalDateTime end);

    // 회원: 특정 트럭에 투표했는지 여부 체크
    boolean existsByUserAndEventAndTruck(User user, Event event, Truck truck);

    // 비회원: 특정 트럭에 투표했는지 여부 체크
    boolean existsByFingerprintAndEventAndTruck(String fingerprint, Event event, Truck truck);

    Optional<Vote> findByUserAndEventAndTruck(User user, Event event, Truck truck);
}
