package com.operator.dto.auth;

public record AuthResponseDto(
        String accessToken,
        String refreshToken,
        String tokenType // Bearer
) {
}
