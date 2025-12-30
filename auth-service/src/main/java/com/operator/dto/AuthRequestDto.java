package com.operator.dto.auth;

public record AuthRequestDto(
        String phoneNumber,
        String otp
) {
}
