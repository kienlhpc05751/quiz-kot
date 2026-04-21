package com.kot.quizkot.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record QuizDetailResponse(
        String id,
        String title,
        String description,
        Integer passingScore,
        String status,
        Boolean publicQuiz,
        Long createdById,
        String createdByName,
        String shareToken,
        LocalDateTime createdAt,
        LocalDateTime expiredAt,
        Integer questionCount,
        Integer attempts,
        String difficulty,
        List<QuizQuestionResponse> questions
) {
}
