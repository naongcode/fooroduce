package com.foodu.Repository;

import com.foodu.Entity.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteResultRepository extends JpaRepository<VoteResult, Integer> {
}