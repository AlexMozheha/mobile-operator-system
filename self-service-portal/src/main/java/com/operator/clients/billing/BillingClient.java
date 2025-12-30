package com.operator.clients.billing;

import com.operator.clients.billing.dto.*;
import com.operator.clients.crm.dto.TariffChangeCommand;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BillingClient {
    Page<TariffDto> getAllTariffs(int page, int size);
    TariffDto getTariffById(Long id);
    void changeCustomerTariff(TariffChangeCommand command);
    BalanceDto getBalanceByCustomerId(Long customerId);
    UsageRecordDto getUsageByCustomerId(Long customerId);
    InvoiceDto createInvoice(InvoiceCreateRequest request);
    void payInvoice(Long invoiceId);
    List<InvoiceDto> getInvoicesByCustomerId(Long customerId);
}
