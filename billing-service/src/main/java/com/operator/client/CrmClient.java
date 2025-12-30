package com.operator.client;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "crm-service", url = "${application.config.crm-url}")
public interface CrmClient {

    @PutMapping("/api/customers/tariff")
    void updateCustomerTariff(@RequestBody CrmTariffChangeRequest request);
}
