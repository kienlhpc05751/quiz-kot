package com.kot.quizkot.dto.request;

public record QuizOptionRequest(
        Long id,
        String content,
        Boolean correct
) {
}