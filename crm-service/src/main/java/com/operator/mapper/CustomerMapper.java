package com.operator.mapper;

import com.operator.dto.crm.CustomerDto;
import com.operator.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto toDto(CustomerEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    CustomerEntity toEntity(CustomerDto dto);
}

