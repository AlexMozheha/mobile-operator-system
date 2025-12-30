package com.operator.clients.billing.dto;

import java.math.BigDecimal;

public record InvoiceCreateRequest(Long customerId, BigDecimal amount) {}
