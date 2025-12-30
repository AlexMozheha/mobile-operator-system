package com.operator.dto;

import java.math.BigDecimal;

public record InvoiceCreateRequest(Long customerId, BigDecimal amount) {}
