package com.operator.client;

import com.operator.dto.TariffChangeCommand;

public interface CrmClient {
    void updateCustomerTariff(TariffChangeCommand request);

    public boolean customerExists(Long customerId);

}