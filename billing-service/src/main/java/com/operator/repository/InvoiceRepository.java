package com.operator.repository;

import com.operator.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity,Long> {

    List<InvoiceEntity> findByCustomerId(Long customerId);
}
