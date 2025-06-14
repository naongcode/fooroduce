package com.foodu.repository;

import com.foodu.entity.TruckApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TruckApplicationRepository extends JpaRepository<TruckApplication, Integer> {

    List<TruckApplication> findByEvent_EventId(Integer eventId);

    // pagination 적용
    Page<TruckApplication> findByEvent_EventId(Integer eventId, Pageable pageable);

    List<TruckApplication> findByTruck_Owner_UserId(String ownerId);

    Optional<TruckApplication> findByApplicationIdAndEvent_EventId(Integer applicationId, Integer eventId);
    
    boolean existsByTruck_TruckIdAndEvent_EventId(Integer truckId, Integer eventId);

    @Query("SELECT DISTINCT ta FROM TruckApplication ta " +
            "JOIN FETCH ta.truck t " +
            "JOIN TruckMenu m ON m.truck.truckId = t.truckId " +
            "WHERE ta.event.eventId = :eventId " +
            "AND (:menuType IS NULL OR m.menuType = :menuType)")
    Page<TruckApplication> findByEventIdAndMenuType(
            @Param("eventId") Integer eventId,
            @Param("menuType") String menuType,
            Pageable pageable);

    Page<TruckApplication> findByEvent_EventIdAndTruck_TruckIdIn(Integer eventId, List<Integer> truckIds, Pageable pageable);

}
