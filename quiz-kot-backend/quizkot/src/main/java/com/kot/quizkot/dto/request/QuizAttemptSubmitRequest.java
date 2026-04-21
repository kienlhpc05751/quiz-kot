package com.kot.quizkot.dto.request;

import java.util.List;

public record QuizAttemptSubmitRequest(
        Long userId,
        List<QuizAnswerRequest> answers
) {
}