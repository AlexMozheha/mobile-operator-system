package com.operator.clients.crm;


import com.operator.clients.crm.dto.CustomerDto;
import com.operator.clients.crm.dto.TariffChangeCommand;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CrmClient {
    Page<CustomerDto> getAllCustomers(int page, int size);
    CustomerDto getCustomerById(Long id);
    void updateCustomerTariff(TariffChangeCommand command);
    Page<CustomerDto> findCustomersByTariff(Long tariffId, int page, int size);
}
