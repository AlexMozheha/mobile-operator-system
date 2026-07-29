package com.operator.service.impl;


import com.operator.client.CrmClient;
import com.operator.dto.CdrRawData;
import com.operator.dto.UsageRecordDto;
import com.operator.entity.BalanceEntity;
import com.operator.entity.UsageRecordEntity;
import com.operator.mapper.TariffMapper;
import com.operator.mapper.UsageRecordMapper;
import com.operator.repository.BalanceRepository;
import com.operator.repository.TariffRepository;
import com.operator.repository.UsageRecordRepository;
import com.operator.service.UsageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UsageServiceImpl implements UsageService {

    private final UsageRecordMapper usageMapper;
    private final UsageRecordRepository usageRepository;
    private final BalanceRepository balanceRepository;
    private final TariffRepository tariffRepository;

    @Override
    @Transactional(readOnly = true)
    public UsageRecordDto getUsageByCustomerId(Long customerId){
        return usageRepository.findById(customerId).map(usageMapper::toDto).
                orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));
    }

//    @Override
//    @Transactional
//    public void processCall(CdrRawData cdr) {
//
//        UsageRecordEntity usage = usageRepository.findByPhoneNumber(cdr.callingNumber())
//                .orElseThrow(() -> new EntityNotFoundException("Calling number not found with id: " + cdr.callingNumber()));
//
//        long minutesUsed = (long) Math.ceil(cdr.durationSeconds() / 60.0);

//        if (usage.getCallMinutes() >= minutesUsed) {
//            usage.setCallMinutes(usage.getCallMinutes() - (int) minutesUsed);
//        } else {
//            long overLimit = minutesUsed - usage.getCallMinutes();
//            usage.setCallMinutes(0);

//            BalanceEntity balance = balanceRepository.findById(usage.getCustomerId())
//                    .orElseThrow(() -> new EntityNotFoundException("Balance not found for customer: " + usage.getCustomerId()));
//            balance.setAmount(balance.getAmount().subtract(new BigDecimal(overLimit * 1.5)));
//            balanceRepository.save(balance);
//        }
//        usageRepository.save(usage);
//    }



}
