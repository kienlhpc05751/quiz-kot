package com.kot.quizkot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kot.quizkot.entity.QuizOption;

@Repository
public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {
}