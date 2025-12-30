package com.operator.repository;

import com.operator.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {

    Optional<CustomerEntity> findByPhoneNumber(String phoneNumber);

    Page<CustomerEntity> findAllByTariffId(Long tariffId, Pageable pageable);

}
