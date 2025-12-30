package com.operator.clients.crm.impl;


import com.operator.clients.crm.CrmClient;
import com.operator.clients.crm.dto.CustomerDto;
import com.operator.clients.crm.dto.TariffChangeCommand;
import com.operator.dto.RestPageImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class CrmClientImpl implements CrmClient {

    private final RestClient client;

    public CrmClientImpl(RestClient.Builder builder) {
        this.client = builder.baseUrl("http://crm-service/api/customers").build();
    }

    @Override
    public Page<CustomerDto> getAllCustomers(int page, int size) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.queryParam("page", page).queryParam("size", size).build())
                .retrieve()
                .body(new ParameterizedTypeReference<RestPageImpl<CustomerDto>>() {});
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        return client.get().uri("/{id}", id).retrieve().body(CustomerDto.class);
    }

    @Override
    public void updateCustomerTariff(TariffChangeCommand command) {
        client.put().uri("/tariff").body(command).retrieve().toBodilessEntity();
    }

    @Override
    public Page<CustomerDto> findCustomersByTariff(Long tariffId, int page, int size) {
        return client.get()
                .uri(uriBuilder -> uriBuilder.path("/search")
                        .queryParam("tariffId", tariffId)
                        .queryParam("page", page)
                        .queryParam("size", size).build())
                .retrieve()
                .body(new ParameterizedTypeReference<RestPageImpl<CustomerDto>>() {});
    }
}
