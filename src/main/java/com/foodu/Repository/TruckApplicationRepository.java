package com.foodu.Repository;

import com.foodu.Entity.TruckApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckApplicationRepository extends JpaRepository<TruckApplication, Integer> {
}