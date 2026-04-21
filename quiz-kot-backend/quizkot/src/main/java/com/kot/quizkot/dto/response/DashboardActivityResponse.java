package com.kot.quizkot.dto.response;

import java.time.LocalDateTime;

public record DashboardActivityResponse(
        Long attemptId,
        String quizId,
        String quizTitle,
        Long userId,
        String username,
        Double score,
        Boolean passed,
        LocalDateTime submittedAt
) {
}