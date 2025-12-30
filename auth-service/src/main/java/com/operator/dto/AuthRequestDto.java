package com.operator.dto;

public record AuthRequestDto(
        String phoneNumber,
        String otp
) {
}
