package com.operator.service.impl;

import com.operator.client.CrmClient;
import com.operator.dto.InvoiceCreateRequest;
import com.operator.dto.InvoiceDto;
import com.operator.entity.BalanceEntity;
import com.operator.entity.InvoiceEntity;
import com.operator.enums.InvoiceStatus;
import com.operator.mapper.InvoiceMapper;
import com.operator.repository.BalanceRepository;
import com.operator.repository.InvoiceRepository;
import com.operator.service.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper mapper;
    private final BalanceRepository balanceRepository;
    private final CrmClient crmClient;

    @Override
    @Transactional
    public InvoiceDto createInvoice(InvoiceCreateRequest request){

        if(!crmClient.customerExists(request.customerId())){
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "No user with this ID found");
        }

        InvoiceEntity entity = InvoiceEntity.builder()
                .customerId(request.customerId())
                .amount(request.amount())
                .issueDate(Instant.now())
                .invoiceStatus(InvoiceStatus.UNPAID).build();

        InvoiceEntity savedInvoice = invoiceRepository.save(entity);

        return mapper.toDto(savedInvoice);
    }

    @Override
    @Transactional
    public void payInvoice(Long invoiceId){

        InvoiceEntity invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + invoiceId));

        if (invoice.getInvoiceStatus() != InvoiceStatus.UNPAID) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Invoice status must be UNPAID");
        }
        BalanceEntity balance = balanceRepository.findById(invoice.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Balance record not found for customer: " + invoice.getCustomerId()));


        if (balance.getAmount().compareTo(invoice.getAmount()) < 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Not enough money");
        }

        balance.setAmount(balance.getAmount().subtract(invoice.getAmount()));
        balance.setLastUpdated(Instant.now());

        invoice.setInvoiceStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(Instant.now());

        balanceRepository.save(balance);
        invoiceRepository.save(invoice);
    }


    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDto> getInvoicesByCustomerId(Long id){
        return invoiceRepository.findByCustomerId(id).stream().map(mapper::toDto).collect(Collectors.toList());
    }


}
