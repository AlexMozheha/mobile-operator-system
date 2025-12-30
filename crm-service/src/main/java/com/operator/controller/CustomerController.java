package com.operator.controller;

import com.operator.dto.CustomerDto;
import com.operator.dto.TariffChangeCommand;
import com.operator.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<Page<CustomerDto>> getAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(customerService.getAllCustomers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/tariff")
    public ResponseEntity<Void> updateTariff(@RequestBody TariffChangeCommand command) {
        customerService.updateCustomerTariff(command);
        return ResponseEntity.ok().build();
    }
}
