package com.kot.quizkot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kot.quizkot.entity.QuizAttempt;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
	List<QuizAttempt> findByQuiz_IdOrderBySubmittedAtDesc(String quizId);
	List<QuizAttempt> findByUser_IdOrderBySubmittedAtDesc(Long userId);
	Optional<QuizAttempt> findTopByUser_IdAndQuiz_IdOrderBySubmittedAtDesc(Long userId, String quizId);
	List<QuizAttempt> findAllByOrderBySubmittedAtDesc();
}