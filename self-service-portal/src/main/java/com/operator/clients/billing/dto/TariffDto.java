package com.operator.clients.billing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TariffDto(
        Long id,
        @NotBlank String name,
        @NotNull @DecimalMin(value = "0.0") BigDecimal monthlyPrice,
        @NotNull @PositiveOrZero Integer minutesPackage,
        @NotNull @PositiveOrZero Integer smsPackage,
        @NotNull @DecimalMin(value = "0.0") BigDecimal gigaBytesPackage
) {
}
