package com.kot.quizkot.dto.response;

public record QuizOptionResponse(
        Long id,
        String content,
        Boolean correct
) {
}