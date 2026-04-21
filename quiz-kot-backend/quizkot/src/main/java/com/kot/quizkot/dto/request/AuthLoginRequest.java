package com.kot.quizkot.dto.request;

public record AuthLoginRequest(
        String email,
        String password
) {
}