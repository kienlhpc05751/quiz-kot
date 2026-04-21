package com.kot.quizkot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kot.quizkot.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
	List<Question> findByQuiz_IdOrderById(String quizId);
}