package com.operator.service;

import com.operator.dto.BalanceDto;

public interface BalanceService {
    BalanceDto getBalanceByCustomerId(Long customerId);
}
