package com.kot.quizkot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kot.quizkot.entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
}