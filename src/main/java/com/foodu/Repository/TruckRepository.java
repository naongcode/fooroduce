package com.foodu.Repository;

import com.foodu.Entity.Truck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckRepository extends JpaRepository<Truck, Integer> {
}