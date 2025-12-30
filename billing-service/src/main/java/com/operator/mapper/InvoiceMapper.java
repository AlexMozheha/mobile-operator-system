package com.operator.mapper;


import com.operator.dto.InvoiceDto;
import com.operator.entity.InvoiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    InvoiceDto toDto(InvoiceEntity entity);
    List<InvoiceDto> toDto(List<InvoiceEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "invoiceStatus", constant = "UNPAID")
    @Mapping(target = "issueDate", expression = "java(java.time.Instant.now())")
    InvoiceEntity toEntity(InvoiceDto dto);

    List<InvoiceEntity> toEntity(List<InvoiceDto> dtos);
}
