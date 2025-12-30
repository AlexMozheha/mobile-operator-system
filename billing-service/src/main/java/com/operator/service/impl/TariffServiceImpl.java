package com.operator.service.impl;

import com.operator.client.CrmClient;
import com.operator.dto.InvoiceCreateRequest;
import com.operator.dto.InvoiceDto;
import com.operator.dto.TariffChangeCommand;
import com.operator.dto.TariffDto;
import com.operator.entity.BalanceEntity;
import com.operator.entity.TariffEntity;
import com.operator.entity.UsageRecordEntity;
import com.operator.mapper.TariffMapper;
import com.operator.repository.BalanceRepository;
import com.operator.repository.TariffRepository;
import com.operator.repository.UsageRecordRepository;
import com.operator.service.InvoiceService;
import com.operator.service.TariffService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final TariffMapper tariffMapper;
    private final BalanceRepository balanceRepository;
    private final UsageRecordRepository usageRepository;
    private final CrmClient crmClient;
    private final InvoiceService invoiceService;

    @Override
    @Transactional(readOnly = true)
    public Page<TariffDto> getAllTariffs(int page, int size){

        return tariffRepository.findAll(PageRequest.of(page,size)).map(tariffMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public TariffDto getTariffById(Long id){

        return tariffRepository.findById(id).map(tariffMapper::toDto).orElseThrow(()-> new EntityNotFoundException("Tariff not found!"));
    }

    @Override
    @Transactional
    public void changeCustomerTariff(TariffChangeCommand command){

        if(!crmClient.customerExists(command.customerId())){
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "No user with this ID found");
        }

        TariffEntity newTariff = tariffRepository.findById(command.newTariffId())
                .orElseThrow(() -> new EntityNotFoundException("Tariff not found"));


        InvoiceDto invoiceDto = invoiceService.createInvoice(new InvoiceCreateRequest(
                command.customerId(),
                newTariff.getMonthlyPrice()
        ));

        invoiceService.payInvoice(invoiceDto.id());

        UsageRecordEntity usage = usageRepository.findById(command.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Usage record not found"));

        usage.setCallMinutes(newTariff.getMinutesPackage());
        usage.setSmsCount(newTariff.getSmsPackage());
        usage.setInternetCount(newTariff.getGbPackage());
        usageRepository.save(usage);

        crmClient.updateCustomerTariff(new TariffChangeCommand(command.customerId(), command.newTariffId()));
    }
}
