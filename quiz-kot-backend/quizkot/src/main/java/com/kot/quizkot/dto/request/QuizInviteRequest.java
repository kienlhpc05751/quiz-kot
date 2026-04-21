package com.kot.quizkot.dto.request;

public record QuizInviteRequest(
        String email,
        String role,
        Long userId
) {
}