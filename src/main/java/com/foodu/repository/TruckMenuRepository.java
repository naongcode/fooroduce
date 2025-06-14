package com.foodu.repository;

import com.foodu.entity.TruckMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TruckMenuRepository extends JpaRepository<TruckMenu, Integer> {
    List<TruckMenu> findByTruck_TruckId(Integer truckId);

    @Query("SELECT DISTINCT tm.truck.truckId " +
            "FROM TruckMenu tm " +
            "JOIN TruckApplication ta ON tm.truck.truckId = ta.truck.truckId " +
            "WHERE ta.event.eventId = :eventId AND tm.menuType = :menuType")
    List<Integer> findDistinctTruckIdsByEventIdAndMenuType(@Param("eventId") Integer eventId,
                                                           @Param("menuType") String menuType);
}
