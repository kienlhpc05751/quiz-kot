package com.kot.quizkot.dto.request;

public record AuthRegisterRequest(
        String name,
        String email,
        String password
) {
}