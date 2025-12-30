package com.operator.dto.billing;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceDto(
        Long customerId,
        @NotNull @DecimalMin(value = "0.0") BigDecimal amount,
        @NotNull Instant lastUpdated
) {
}
