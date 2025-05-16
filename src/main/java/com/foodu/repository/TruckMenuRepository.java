package com.foodu.repository;

import com.foodu.entity.TruckMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TruckMenuRepository extends JpaRepository<TruckMenu, Integer> {
    List<TruckMenu> findByTruck_TruckId(Integer truckId);
}
