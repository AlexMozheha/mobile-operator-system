package com.operator.mapper;


import com.operator.dto.TariffDto;
import com.operator.entity.TariffEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TariffMapper {
    @Mapping(target = "gigaBytesPackage", source = "gbPackage")
    TariffDto toDto(TariffEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gbPackage", source = "gigaBytesPackage")
    TariffEntity toEntity(TariffDto dto);
}
