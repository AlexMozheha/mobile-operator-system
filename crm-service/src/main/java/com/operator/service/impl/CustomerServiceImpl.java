package com.operator.service.impl;


import com.operator.dto.TariffChangeCommand;
import com.operator.dto.crm.CustomerDto;
import com.operator.entity.CustomerEntity;
import com.operator.mapper.CustomerMapper;
import com.operator.repository.CustomerRepository;
import com.operator.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id){
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> getAllCustomers(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);

        return repository.findAll(pageRequest).map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> findCustomersByTariff(Long findAllByTariffId, int page, int size){
        return repository.findAllByTariffId(findAllByTariffId, PageRequest.of(page, size)).map(mapper::toDto);
    }

    @Override
    @Transactional
    public void updateCustomerTariff(TariffChangeCommand  command){

        CustomerEntity customer = repository.findById(command.customerId()).orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setTariffId(command.newTariffId());

        repository.save(customer);

    }

}
