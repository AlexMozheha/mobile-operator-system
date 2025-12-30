package com.operator.client.impl;


import com.operator.client.CrmClient;
import com.operator.dto.TariffChangeCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class CrmClientImpl implements CrmClient {

    private final RestClient client;

    public CrmClientImpl(RestClient.Builder builder) {
        this.client = builder.baseUrl("http://crm-service").build();
    }

    @Override
    public void updateCustomerTariff(TariffChangeCommand request) {
        try {
            log.info("Sending tariff update request for customer to CRM: {}", request);

            client.put()
                    .uri("/api/customers/tariff")
                    .body(request)
                    .retrieve()
                    .toBodilessEntity();

            log.info("Successfully updated tariff in CRM");
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            log.error("Error calling CRM Service (Status: {}): {}", e.getStatusCode(), e.getResponseBodyAsString());

            throw new RuntimeException("CRM service returned error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Unexpected error while calling CRM Service: {}", e.getMessage());

            throw new RuntimeException("Unexpected error during CRM call", e);
        }
    }

    @Override
    public boolean customerExists(Long customerId) {
        try {
            return client.get()
                    .uri("/api/customers/{id}", customerId)
                    .retrieve()
                    .toBodilessEntity()
                    .getStatusCode()
                    .is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

}
