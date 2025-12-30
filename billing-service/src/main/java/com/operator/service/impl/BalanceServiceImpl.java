package com.operator.service.impl;

import com.operator.dto.BalanceDto;
import com.operator.mapper.BalanceMapper;
import com.operator.repository.BalanceRepository;
import com.operator.service.BalanceService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceMapper mapper;
    private final BalanceRepository repository;

    @Override
    @Transactional(readOnly = true)
    public BalanceDto getBalanceByCustomerId(Long customerId){
        return repository.findById(customerId).map(mapper::toDto).orElseThrow(() -> new RuntimeException("CustomerId not found"));
    }
}
