package com.foodu.repository;

import com.foodu.entity.TruckApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface TruckApplicationRepository extends JpaRepository<TruckApplication, Integer> {
    List<TruckApplication> findByEvent_EventId(Integer eventId);

    List<TruckApplication> findByTruck_Owner_UserId(String ownerId);

    Optional<TruckApplication> findByApplicationIdAndEvent_EventId(Integer applicationId, Integer eventId);


}
