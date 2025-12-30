package com.operator.service;


import com.operator.dto.TariffChangeCommand;
import com.operator.dto.CustomerDto;
import org.springframework.data.domain.Page;


public interface CustomerService {

    public CustomerDto getCustomerById(Long id);
    public Page<CustomerDto> getAllCustomers(int page, int size);
    public void updateCustomerTariff(TariffChangeCommand command);
    public Page<CustomerDto> findCustomersByTariff(Long tariffId, int page, int size);

}
