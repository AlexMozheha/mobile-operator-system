package com.operator.mapper;


import com.operator.dto.UsageRecordDto;
import com.operator.entity.UsageRecordEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsageRecordMapper {

    UsageRecordDto toDto(UsageRecordEntity entity);

 //   @Mapping(target = "id", ignore = true)
    UsageRecordEntity toEntity(UsageRecordDto dto);
}
