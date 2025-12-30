package com.operator.clients.crm.dto;

public record TariffChangeCommand(
        Long customerId,
        Long newTariffId
) {
}
