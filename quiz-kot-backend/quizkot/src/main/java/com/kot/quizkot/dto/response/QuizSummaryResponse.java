package com.kot.quizkot.dto.response;

public record QuizSummaryResponse(
        String id,
        String title,
        String description,
        Integer attempts,
        String difficulty,
        Integer questionCount,
        Integer passingScore
) {
}