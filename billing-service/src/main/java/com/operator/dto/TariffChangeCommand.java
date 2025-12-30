package com.operator.dto;

public record TariffChangeCommand(
        Long customerId,
        Long newTariffId
) {
}
