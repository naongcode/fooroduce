package com.foodu.repository;

import com.foodu.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;


public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByLocationContaining(String location);

    // 투표 마감일이 현재 날짜 이후인 경우 => 현재 투표 가능한 행사
    @Query("SELECT e FROM Event e WHERE e.voteEnd >= :today")
    List<Event> findOngoingEvents(@Param("today") LocalDateTime today);

    // 행사 종료일이 현재 날짜 이전인 경우 => 종료된 행사
    @Query("SELECT e FROM Event e WHERE e.eventEnd < :today")
    List<Event> findClosedEvents(@Param("today") LocalDateTime today);

    //이미지 이름이 같은거 세는 로직
    @Query("SELECT COUNT(e) FROM Event e WHERE e.eventImage LIKE CONCAT(:prefix, '%')")
    int countByEventImagePrefix(@Param("prefix") String prefix);

    // pageable 관련
    @Query("SELECT e FROM Event e WHERE e.voteEnd >= :today")
    Page<Event> findOngoingEvents(@Param("today") LocalDateTime today, Pageable pageable);

    List<Event> findByCreatedBy(String createdBy);
}
