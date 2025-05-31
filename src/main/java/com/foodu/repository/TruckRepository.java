package com.foodu.repository;

import com.foodu.entity.Event;
import com.foodu.entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Integer> {

    @Query("SELECT ta.truck FROM TruckApplication ta WHERE ta.event.id = :eventId")
    List<Truck> findTrucksByEventId(@Param("eventId") Integer eventId);

    List<Truck> findByOwner_UserId(String ownerId);

}
