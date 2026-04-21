package com.kot.quizkot.dto.response;

public record AuthResponse(
        String token,
        String accessToken,
        UserResponse user
) {
}