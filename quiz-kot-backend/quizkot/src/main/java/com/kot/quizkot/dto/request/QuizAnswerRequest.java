package com.kot.quizkot.dto.request;

public record QuizAnswerRequest(
        String questionId,
        Long selectedOptionId,
        Integer selectedIndex
) {
}