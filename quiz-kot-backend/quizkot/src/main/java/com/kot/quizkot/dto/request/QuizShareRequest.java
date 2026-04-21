package com.kot.quizkot.dto.request;

public record QuizShareRequest(
        Boolean publicQuiz,
        String shareToken,
        String expiredAt,
        String role
) {
}