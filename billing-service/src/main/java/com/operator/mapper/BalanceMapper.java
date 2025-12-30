package com.operator.mapper;


import com.operator.dto.BalanceDto;
import com.operator.entity.BalanceEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    BalanceDto toDto(BalanceEntity balance);
    List<BalanceDto> toDto(List<BalanceEntity> balances);

    BalanceEntity toEntity(BalanceDto dto);

    List<BalanceEntity> toEntity(List<BalanceDto> balances);
}
