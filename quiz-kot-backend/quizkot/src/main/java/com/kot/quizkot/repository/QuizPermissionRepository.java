package com.kot.quizkot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kot.quizkot.entity.QuizPermission;

@Repository
public interface QuizPermissionRepository extends JpaRepository<QuizPermission, Long> {
	List<QuizPermission> findByQuiz_Id(String quizId);
	long countByQuiz_Id(String quizId);
}