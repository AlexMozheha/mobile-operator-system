package com.operator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.operator.dto.*;
import com.operator.service.BalanceService;
import com.operator.service.InvoiceService;
import com.operator.service.TariffService;
import com.operator.service.UsageService;
import com.operator.validation.JsonAgainstSchemaValidator;
import com.operator.validation.XmlAgainstSchemaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {
    private final TariffService tariffService;
    private final BalanceService balanceService;
    private final InvoiceService invoiceService;
    private final UsageService usageService;


    @GetMapping("/tariff")
    public ResponseEntity<Page<TariffDto>> getTariffs(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(tariffService.getAllTariffs(page, size));
    }

    @PostMapping("/tariff/change")
    public ResponseEntity<Void> changeTariff(@RequestBody TariffChangeCommand command) {
        tariffService.changeCustomerTariff(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance/{customerId}")
    public ResponseEntity<BalanceDto> getBalance(@PathVariable Long customerId) {
        return ResponseEntity.ok(balanceService.getBalanceByCustomerId(customerId));
    }

    @GetMapping("/usage/{customerId}")
    public ResponseEntity<UsageRecordDto> getUsage(@PathVariable Long customerId) {
        return ResponseEntity.ok(usageService.getUsageByCustomerId(customerId));
    }

    @PostMapping("/invoice/{id}/pay")
    public ResponseEntity<Void> payInvoice(@PathVariable Long id) {
        invoiceService.payInvoice(id);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/invoice/create")
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceCreateRequest request) {
        return ResponseEntity.ok(invoiceService.createInvoice(request));
    }

    @GetMapping("/invoice/{customerId}")
    public ResponseEntity<List<InvoiceDto>> getInvoices(@PathVariable Long customerId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByCustomerId(customerId));
    }

    @GetMapping("/tariff/{id}")
    public ResponseEntity<TariffDto> getTariffById(@PathVariable Long id) {
        return ResponseEntity.ok(tariffService.getTariffById(id));
    }

//    @PostMapping("/debug/process-call")
//    public ResponseEntity<Void> simulateCall(@RequestBody CdrRawData cdr) {
//        usageService.processCall(cdr); // метод, який ми обговорювали раніше
//        return ResponseEntity.ok().build();
//    }
}
