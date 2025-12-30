package com.operator.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.Instant;

public record UsageRecordDto(
 //       Long id,
        @NotNull Long customerId,
        @NotNull @PositiveOrZero Integer callMinutes,
        @NotNull @PositiveOrZero Integer smsCount,
        @NotNull @PositiveOrZero BigDecimal internetCount,
        @NotNull Instant usageDate
) {
}
