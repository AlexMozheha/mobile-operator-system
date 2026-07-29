package com.operator.dto;

// will be implemented in the future
public record AuthResponseDto(
        String accessToken,
        String refreshToken,
        String tokenType // Bearer
) {
}
