package com.operator.service;

import com.operator.dto.CdrRawData;
import com.operator.dto.UsageRecordDto;

public interface UsageService {
    UsageRecordDto getUsageByCustomerId(Long customerId);

    // public void processCall(CdrRawData cdr);
}
