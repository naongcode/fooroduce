package com.foodu.repository;

import com.foodu.entity.VoteResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteResultRepository extends JpaRepository<VoteResult, Integer> {
}
