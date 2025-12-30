package com.operator.clients.billing.impl;


import com.operator.clients.billing.BillingClient;
import com.operator.clients.billing.dto.*;
import com.operator.clients.crm.dto.TariffChangeCommand;
import com.operator.dto.RestPageImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Slf4j
public class BillingClientImpl implements BillingClient {
    private final RestClient client;

    public BillingClientImpl(RestClient.Builder builder) {
        this.client = builder.baseUrl("http://billing-service/api/billing").build();
    }

    @Override
    public Page<TariffDto> getAllTariffs(int page, int size) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/tariff")
                        .queryParam("page", page)
                        .queryParam("size", size).build())
                .retrieve()
                .body(new ParameterizedTypeReference<RestPageImpl<TariffDto>>() {});
    }

    @Override
    public TariffDto getTariffById(Long id) {
        return client.get().uri("/tariff/{id}", id).retrieve().body(TariffDto.class);
    }

    @Override
    public void changeCustomerTariff(TariffChangeCommand command) {
        client.post().uri("/tariff/change").body(command).retrieve().toBodilessEntity();
    }

    @Override
    public BalanceDto getBalanceByCustomerId(Long customerId) {
        return client.get().uri("/balance/{customerId}", customerId).retrieve().body(BalanceDto.class);
    }

    @Override
    public UsageRecordDto getUsageByCustomerId(Long customerId) {
        return client.get().uri("/usage/{customerId}", customerId).retrieve().body(UsageRecordDto.class);
    }

    @Override
    public InvoiceDto createInvoice(InvoiceCreateRequest request) {
        return client.post().uri("/invoice/create").body(request).retrieve().body(InvoiceDto.class);
    }

    @Override
    public void payInvoice(Long invoiceId) {
        client.post().uri("/invoice/{id}/pay", invoiceId).retrieve().toBodilessEntity();
    }

    @Override
    public List<InvoiceDto> getInvoicesByCustomerId(Long customerId) {
        return client.get().uri("/invoice/{customerId}", customerId).retrieve()
                .body(new ParameterizedTypeReference<List<InvoiceDto>>() {});
    }

}
