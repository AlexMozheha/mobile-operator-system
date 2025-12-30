package com.operator.repository;

import com.operator.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {
}
