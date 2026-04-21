package com.kot.quizkot.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QuizQuestionRequest(
        String id,
        String text,
        String type,
        String explanation,
        List<QuizOptionRequest> options
) {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public static QuizQuestionRequest fromJson(
                        @JsonProperty("id") String id,
                        @JsonProperty("text") String text,
                        @JsonProperty("type") String type,
                        @JsonProperty("explanation") String explanation,
                        @JsonProperty("options") List<Object> options,
                        @JsonProperty("correctAnswerIndex") Integer correctAnswerIndex) {
                List<QuizOptionRequest> parsedOptions = null;
                if (options != null) {
                        List<QuizOptionRequest> normalizedOptions = options.stream()
                                        .map(option -> {
                                                if (option instanceof String content) {
                                                        return new QuizOptionRequest(null, content, false);
                                                }

                                                if (option instanceof java.util.Map<?, ?> optionMap) {
                                                        Object idValue = optionMap.get("id");
                                                        Object contentValue = optionMap.get("content");
                                                        Object correctValue = optionMap.get("correct");
                                                        Long optionId = idValue instanceof Number number ? number.longValue() : null;
                                                        String optionContent = contentValue == null ? null : String.valueOf(contentValue);
                                                        Boolean correct = correctValue instanceof Boolean bool ? bool : null;
                                                        return new QuizOptionRequest(optionId, optionContent, correct);
                                                }

                                                return new QuizOptionRequest(null, option == null ? null : String.valueOf(option), false);
                                        })
                                        .toList();

                        if (correctAnswerIndex != null && correctAnswerIndex >= 0 && correctAnswerIndex < normalizedOptions.size()) {
                                final List<QuizOptionRequest> baseOptions = normalizedOptions;
                                final int correctIndex = correctAnswerIndex;
                                normalizedOptions = java.util.stream.IntStream.range(0, baseOptions.size())
                                                .mapToObj(index -> {
                                                        QuizOptionRequest option = baseOptions.get(index);
                                                        return new QuizOptionRequest(
                                                                        option.id(),
                                                                        option.content(),
                                                                        index == correctIndex);
                                                })
                                                .toList();
                        }

                        parsedOptions = normalizedOptions;
                }

                return new QuizQuestionRequest(id, text, type, explanation, parsedOptions);
        }
}