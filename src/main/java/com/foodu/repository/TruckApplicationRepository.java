package com.foodu.repository;

import com.foodu.entity.TruckApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TruckApplicationRepository extends JpaRepository<TruckApplication, Integer> {
    List<TruckApplication> findByEvent_EventId(Integer eventId);
}
