package com.operator.service;

import com.operator.dto.InvoiceCreateRequest;
import com.operator.dto.InvoiceDto;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {
    InvoiceDto createInvoice(InvoiceCreateRequest request);
    void payInvoice(Long invoiceId);

    List<InvoiceDto> getInvoicesByCustomerId(Long id);


}
