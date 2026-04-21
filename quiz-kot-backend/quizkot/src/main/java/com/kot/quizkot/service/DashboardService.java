package com.kot.quizkot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kot.quizkot.dto.response.ApiResponse;
import com.kot.quizkot.dto.response.DashboardActivityResponse;
import com.kot.quizkot.dto.response.DashboardSummaryResponse;
import com.kot.quizkot.entity.QuizAttempt;
import com.kot.quizkot.repository.QuizAttemptRepository;
import com.kot.quizkot.repository.QuizRepository;
import com.kot.quizkot.repository.UserRepository;
import com.kot.quizkot.util.ResponseFactory;

@Service
public class DashboardService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public ApiResponse getSummary() {
        try {
            long totalQuizzes = quizRepository.count();
            long totalAttempts = quizAttemptRepository.count();
            double averageScore = quizAttemptRepository.findAll().stream()
                    .mapToDouble(attempt -> attempt.getScore() == null ? 0.0 : attempt.getScore())
                    .average()
                    .orElse(0.0);
            double passRate = totalAttempts == 0 ? 0.0 : (
                    quizAttemptRepository.findAll().stream().filter(attempt -> Boolean.TRUE.equals(attempt.getPassed())).count() * 100.0 / totalAttempts
            );

            return ResponseFactory.success(
                    new DashboardSummaryResponse(totalQuizzes, totalAttempts, roundTwoDecimals(averageScore), roundTwoDecimals(passRate), userRepository.count()),
                    "Dashboard summary retrieved successfully"
            );
        } catch (Exception e) {
            return ResponseFactory.error("Failed to load dashboard summary: " + e.getMessage(), 500);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse getActivity() {
        try {
            List<DashboardActivityResponse> activity = quizAttemptRepository.findAllByOrderBySubmittedAtDesc().stream()
                    .limit(10)
                    .map(this::toActivityResponse)
                    .toList();

            return ResponseFactory.success(activity, "Dashboard activity retrieved successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to load dashboard activity: " + e.getMessage(), 500);
        }
    }

    private DashboardActivityResponse toActivityResponse(QuizAttempt attempt) {
        return new DashboardActivityResponse(
                attempt.getId(),
                attempt.getQuiz() == null ? null : attempt.getQuiz().getId(),
                attempt.getQuiz() == null ? null : attempt.getQuiz().getTitle(),
                attempt.getUser() == null ? null : attempt.getUser().getId(),
                attempt.getUser() == null ? null : attempt.getUser().getUsername(),
                attempt.getScore(),
                attempt.getPassed(),
                attempt.getSubmittedAt()
        );
    }

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}