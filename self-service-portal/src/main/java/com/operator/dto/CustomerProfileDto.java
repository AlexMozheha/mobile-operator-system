package com.operator.dto;

import com.operator.clients.billing.dto.BalanceDto;
import com.operator.clients.billing.dto.InvoiceDto;
import com.operator.clients.billing.dto.TariffDto;
import com.operator.clients.billing.dto.UsageRecordDto;
import com.operator.clients.crm.dto.CustomerDto;


import java.util.List;

public record CustomerProfileDto(
        CustomerDto customer,
        BalanceDto balance,
        TariffDto tariff,
        UsageRecordDto usage,
        List<InvoiceDto> recentInvoices
) {}
