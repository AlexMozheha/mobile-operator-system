package com.operator.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.math.BigDecimal;

@JacksonXmlRootElement(localName = "invoice")
public record InvoiceCreateRequest(Long customerId, BigDecimal amount) {}
