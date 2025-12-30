package com.operator.dto;

public record AuthResponseDto(
        String accessToken,
        String refreshToken,
        String tokenType // Bearer
) {
}
