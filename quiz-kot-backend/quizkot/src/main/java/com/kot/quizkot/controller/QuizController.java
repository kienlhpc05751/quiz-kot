package com.kot.quizkot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kot.quizkot.dto.request.QuizAttemptSubmitRequest;
import com.kot.quizkot.dto.request.QuizCreateRequest;
import com.kot.quizkot.dto.request.QuizInviteRequest;
import com.kot.quizkot.dto.request.QuizShareRequest;
import com.kot.quizkot.dto.response.ApiResponse;
import com.kot.quizkot.service.QuizService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping
    public ApiResponse<?> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getQuizById(@PathVariable String id) {
        return quizService.getQuizById(id);
    }

    @GetMapping("/{id}/questions")
    public ApiResponse<?> getQuizQuestions(@PathVariable String id) {
        return quizService.getQuizQuestions(id);
    }

    @PostMapping
    public ApiResponse<?> createQuiz(@RequestBody QuizCreateRequest request) {
        return quizService.createQuiz(request);
    }

    // create api import quiz from json file 
    @PostMapping("/import")
    public ApiResponse<?> importQuiz(@RequestPart("file") MultipartFile file) {
        return quizService.importQuiz(file);
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateQuiz(@PathVariable String id, @RequestBody QuizCreateRequest request) {
        return quizService.updateQuiz(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteQuiz(@PathVariable String id) {
        return quizService.deleteQuiz(id);
    }

    @PostMapping("/{id}/attempts")
    public ApiResponse<?> submitAttempt(@PathVariable String id, @RequestBody QuizAttemptSubmitRequest request) {
        return quizService.submitAttempt(id, request);
    }

    @GetMapping("/{id}/attempts/me")
    public ApiResponse<?> getMyLatestAttempt(@PathVariable String id, @RequestParam(required = false) Long userId) {
        return quizService.getLatestAttempt(id, userId);
    }

    @PostMapping("/{id}/share")
    public ApiResponse<?> shareQuiz(@PathVariable String id, @RequestBody QuizShareRequest request) {
        return quizService.shareQuiz(id, request);
    }

    @PostMapping("/{id}/invites")
    public ApiResponse<?> inviteUser(@PathVariable String id, @RequestBody QuizInviteRequest request) {
        return quizService.inviteUser(id, request);
    }
}
