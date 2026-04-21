package com.kot.quizkot.dto.response;

import java.time.LocalDateTime;

public record QuizAttemptResponse(
        Long id,
        String quizId,
        String quizTitle,
        Long userId,
        Double score,
        Boolean passed,
        Integer correctCount,
        Integer wrongCount,
        Integer total,
        LocalDateTime startedAt,
        LocalDateTime submittedAt
) {
}