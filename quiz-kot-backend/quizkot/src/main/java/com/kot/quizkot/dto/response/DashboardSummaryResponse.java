package com.kot.quizkot.dto.response;

public record DashboardSummaryResponse(
        Long totalQuizzes,
        Long totalAttempts,
        Double averageScore,
        Double passRate,
        Long totalUsers
) {
}