package com.kot.quizkot.dto.response;

import java.util.List;

public record QuizQuestionResponse(
        String id,
        String text,
        String type,
        String explanation,
        List<QuizOptionResponse> options
) {
}