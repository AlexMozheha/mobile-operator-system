package com.operator.repository;

import com.operator.entity.UsageRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UsageRecordRepository extends JpaRepository<UsageRecordEntity,Long> {

    //Optional<UsageRecordEntity> findByPhoneNumber(String phoneNumber);

}
