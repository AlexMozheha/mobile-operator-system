package com.operator.client;


import com.operator.dto.crm.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/customers")
public interface CrmClient {

    @GetMapping
    ResponseEntity<List<CustomerDto>> getAllCustomers();

    @GetMapping("/{id}")
    ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") Long id);

    @PostMapping
    ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto);

    @PutMapping("/{customerId}/tariff/{tariffId}")
    ResponseEntity<Void> updateCustomerTariff(
            @PathVariable("customerId") Long customerId,
            @PathVariable("tariffId") Long tariffId);

    @GetMapping("/search")
    ResponseEntity<List<CustomerDto>> findCustomersByTariff(@RequestParam("tariffId") Long tariffId);
}
