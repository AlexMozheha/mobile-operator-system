package com.operator.service;


import com.operator.dto.TariffChangeCommand;
import com.operator.dto.crm.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CustomerService {

    public CustomerDto getCustomerById(Long id);
    public Page<CustomerDto> getAllCustomers(int page, int size);
    public void updateCustomerTariff(TariffChangeCommand command);
    public Page<CustomerDto> findCustomersByTariff(Long tariffId, int page, int size);

}
