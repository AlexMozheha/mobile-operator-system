package com.operator.service;

import com.operator.dto.TariffChangeCommand;
import com.operator.dto.TariffDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TariffService {
    Page<TariffDto> getAllTariffs(int page, int size);
    TariffDto getTariffById(Long id);

    void changeCustomerTariff(TariffChangeCommand command);


}
