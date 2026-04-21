package com.kot.quizkot.dto.request;

import java.util.List;

// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import tools.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QuizCreateRequest(
        String id,
        String title,
        String description,
        Integer passingScore,
        Boolean publicQuiz,
        String status,
        Long createdById,
        List<QuizQuestionRequest> questions
) {
    public static QuizCreateRequest fromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, QuizCreateRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse quiz create request from json: " + e.getMessage(), e);
        }
    }
}