package com.operator.dto;

import java.math.BigDecimal;

public record PaymentRequest(
        String cardNumber,
        BigDecimal amount,
        String cvvCode
) {
}
