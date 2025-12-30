package com.operator.dto;

import com.operator.enums.InvoiceStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record InvoiceDto(
        Long id,
        Long customerId,
        @NotNull @DecimalMin(value = "0.0") BigDecimal amount,
        @NotNull Instant issueDate,
        Instant paidAt,
        @NotNull InvoiceStatus status
) {
}
