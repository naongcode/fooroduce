package com.foodu.Repository;

import com.foodu.Entity.TruckMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckMenuRepository extends JpaRepository<TruckMenu, Integer> {
}