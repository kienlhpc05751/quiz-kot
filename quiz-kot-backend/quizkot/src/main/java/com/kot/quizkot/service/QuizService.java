package com.kot.quizkot.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kot.quizkot.dto.request.QuizAnswerRequest;
import com.kot.quizkot.dto.request.QuizAttemptSubmitRequest;
import com.kot.quizkot.dto.request.QuizCreateRequest;
import com.kot.quizkot.dto.request.QuizInviteRequest;
import com.kot.quizkot.dto.request.QuizOptionRequest;
import com.kot.quizkot.dto.request.QuizQuestionRequest;
import com.kot.quizkot.dto.request.QuizShareRequest;
import com.kot.quizkot.dto.response.ApiResponse;
import com.kot.quizkot.dto.response.QuizAttemptResponse;
import com.kot.quizkot.dto.response.QuizDetailResponse;
import com.kot.quizkot.dto.response.QuizOptionResponse;
import com.kot.quizkot.dto.response.QuizQuestionResponse;
import com.kot.quizkot.dto.response.QuizSummaryResponse;
import com.kot.quizkot.entity.PermissionRole;
import com.kot.quizkot.entity.Question;
import com.kot.quizkot.entity.QuestionType;
import com.kot.quizkot.entity.Quiz;
import com.kot.quizkot.entity.QuizAttempt;
import com.kot.quizkot.entity.QuizOption;
import com.kot.quizkot.entity.QuizPermission;
import com.kot.quizkot.entity.QuizStatus;
import com.kot.quizkot.entity.User;
import com.kot.quizkot.entity.UserAnswer;
import com.kot.quizkot.repository.QuestionRepository;
import com.kot.quizkot.repository.QuizAttemptRepository;
import com.kot.quizkot.repository.QuizOptionRepository;
import com.kot.quizkot.repository.QuizPermissionRepository;
import com.kot.quizkot.repository.QuizRepository;
import com.kot.quizkot.repository.UserRepository;
import com.kot.quizkot.util.ResponseFactory;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizOptionRepository quizOptionRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private QuizPermissionRepository quizPermissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public ApiResponse<?> getAllQuizzes() {
        try {
            List<QuizSummaryResponse> quizzes = quizRepository.findAll().stream()
                    .map(this::toSummaryResponse)
                    .toList();
            return ResponseFactory.success(quizzes, "Quizzes retrieved successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to load quizzes: " + e.getMessage(), 500);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<?> getQuizById(String id) {
        try {
            return quizRepository.findById(id)
                    .<ApiResponse<?>>map(
                            quiz -> ResponseFactory.success(toDetailResponse(quiz), "Quiz retrieved successfully"))
                    .orElseGet(() -> ResponseFactory.error("Quiz not found", 404));
        } catch (Exception e) {
            return ResponseFactory.error("Failed to load quiz: " + e.getMessage(), 500);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<?> getQuizQuestions(String id) {
        try {
            if (!quizRepository.existsById(id)) {
                return ResponseFactory.error("Quiz not found", 404);
            }

            List<QuizQuestionResponse> questions = questionRepository.findByQuiz_IdOrderById(id).stream()
                    .map(this::toQuestionResponse)
                    .toList();
            return ResponseFactory.success(questions, "Quiz questions retrieved successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to load questions: " + e.getMessage(), 500);
        }
    }

    @Transactional
    public ApiResponse<?> createQuiz(QuizCreateRequest request) {
        try {
            Quiz quiz = new Quiz();
            quiz.setId(request.id() == null || request.id().isBlank() ? generateQuizId() : request.id());
            quiz.setTitle(request.title());
            quiz.setDescription(request.description());
            quiz.setPassingScore(request.passingScore() == null ? 70 : request.passingScore());
            quiz.setPublicQuiz(request.publicQuiz() != null && request.publicQuiz());
            quiz.setStatus(parseStatus(request.status()));
            quiz.setShareToken(generateShareToken());
            quiz.setCreatedBy(resolveUser(request.createdById()));

            if (request.questions() != null) {
                quiz.getQuestions().addAll(mapQuestions(request.questions(), quiz));
            }

            Quiz saved = quizRepository.save(quiz);
            return ResponseFactory.success(toDetailResponse(saved), "Quiz created successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to create quiz: " + e.getMessage(), 500);
        }
    }

    @Transactional
    public ApiResponse<?> updateQuiz(String id, QuizCreateRequest request) {
        try {
            Quiz quiz = quizRepository.findById(id).orElse(null);
            if (quiz == null) {
                return ResponseFactory.error("Quiz not found", 404);
            }

            if (request.title() != null) {
                quiz.setTitle(request.title());
            }
            if (request.description() != null) {
                quiz.setDescription(request.description());
            }
            if (request.passingScore() != null) {
                quiz.setPassingScore(request.passingScore());
            }
            if (request.publicQuiz() != null) {
                quiz.setPublicQuiz(request.publicQuiz());
            }
            if (request.status() != null) {
                quiz.setStatus(parseStatus(request.status()));
            }
            if (request.questions() != null) {
                quiz.getQuestions().clear();
                quiz.getQuestions().addAll(mapQuestions(request.questions(), quiz));
            }

            Quiz saved = quizRepository.save(quiz);
            return ResponseFactory.success(toDetailResponse(saved), "Quiz updated successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to update quiz: " + e.getMessage(), 500);
        }
    }

    @Transactional
    public ApiResponse<?> deleteQuiz(String id) {
        try {
            if (!quizRepository.existsById(id)) {
                return ResponseFactory.error("Quiz not found", 404);
            }

            quizRepository.deleteById(id);
            return ResponseFactory.success(null, "Quiz deleted successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to delete quiz: " + e.getMessage(), 500);
        }
    }

    @Transactional
    public ApiResponse<?> submitAttempt(String quizId, QuizAttemptSubmitRequest request) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElse(null);
            if (quiz == null) {
                return ResponseFactory.error("Quiz not found", 404);
            }

            User user = resolveUser(request.userId());
            QuizAttempt attempt = QuizAttempt.builder()
                    .quiz(quiz)
                    .user(user)
                    .startedAt(LocalDateTime.now())
                    .submittedAt(LocalDateTime.now())
                    .build();

            Map<String, Question> questionsById = questionRepository.findByQuiz_IdOrderById(quizId).stream()
                    .collect(Collectors.toMap(
                            Question::getId,
                            question -> question,
                            (first, second) -> first,
                            LinkedHashMap::new));

            int correctCount = 0;
            List<QuizAnswerRequest> answers = request.answers() == null ? List.of() : request.answers();

            for (QuizAnswerRequest answer : answers) {
                Question question = questionsById.get(answer.questionId());
                if (question == null) {
                    continue;
                }

                QuizOption selectedOption = null;
                if (answer.selectedOptionId() != null) {
                    selectedOption = quizOptionRepository.findById(answer.selectedOptionId()).orElse(null);
                }

                boolean isCorrect = selectedOption != null && Boolean.TRUE.equals(selectedOption.getCorrect());
                if (isCorrect) {
                    correctCount++;
                }

                attempt.getUserAnswers().add(UserAnswer.builder()
                        .attempt(attempt)
                        .question(question)
                        .selectedOption(selectedOption)
                        .correct(isCorrect)
                        .build());
            }

            int total = Math.max(questionsById.size(), answers.size());
            int wrongCount = Math.max(total - correctCount, 0);
            double score = total == 0 ? 0.0 : Math.round((correctCount * 100.0 / total) * 100.0) / 100.0;
            attempt.setScore(score);
            attempt.setPassed(score >= (quiz.getPassingScore() == null ? 70 : quiz.getPassingScore()));

            QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

            return ResponseFactory.success(
                    toAttemptResponse(savedAttempt, correctCount, wrongCount, total),
                    "Quiz attempt submitted successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to submit attempt: " + e.getMessage(), 500);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse<?> getLatestAttempt(String quizId, Long userId) {
        try {
            if (!quizRepository.existsById(quizId)) {
                return ResponseFactory.error("Quiz not found", 404);
            }

            QuizAttempt attempt = null;
            if (userId != null) {
                attempt = quizAttemptRepository.findTopByUser_IdAndQuiz_IdOrderBySubmittedAtDesc(userId, quizId)
                        .orElse(null);
            }
            if (attempt == null) {
                attempt = quizAttemptRepository.findByQuiz_IdOrderBySubmittedAtDesc(quizId).stream().findFirst()
                        .orElse(null);
            }

            if (attempt == null) {
                return ResponseFactory.error("Attempt not found", 404);
            }

            int total = questionRepository.findByQuiz_IdOrderById(quizId).size();
            int correctCount = attempt.getUserAnswers().stream()
                    .filter(answer -> Boolean.TRUE.equals(answer.getCorrect())).mapToInt(_ignored -> 1).sum();
            int wrongCount = Math.max(total - correctCount, 0);

            return ResponseFactory.success(toAttemptResponse(attempt, correctCount, wrongCount, total),
                    "Quiz attempt retrieved successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to load attempt: " + e.getMessage(), 500);
        }
    }

    @Transactional
    public ApiResponse<?> shareQuiz(String quizId, QuizShareRequest request) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElse(null);
            if (quiz == null) {
                return ResponseFactory.error("Quiz not found", 404);
            }

            if (request.publicQuiz() != null) {
                quiz.setPublicQuiz(request.publicQuiz());
            }
            if (request.shareToken() != null && !request.shareToken().isBlank()) {
                quiz.setShareToken(request.shareToken());
            } else if (quiz.getShareToken() == null || quiz.getShareToken().isBlank()) {
                quiz.setShareToken(generateShareToken());
            }
            if (request.expiredAt() != null && !request.expiredAt().isBlank()) {
                try {
                    quiz.setExpiredAt(LocalDateTime.parse(request.expiredAt()));
                } catch (Exception ignored) {
                    // Keep current value if parsing fails.
                }
            }

            quizRepository.save(quiz);
            return ResponseFactory.success(toDetailResponse(quiz), "Quiz share settings updated successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to update share settings: " + e.getMessage(), 500);
        }
    }

    @Transactional
    public ApiResponse<?> inviteUser(String quizId, QuizInviteRequest request) {
        try {
            Quiz quiz = quizRepository.findById(quizId).orElse(null);
            if (quiz == null) {
                return ResponseFactory.error("Quiz not found", 404);
            }

            AtomicReference<User> userRef = new AtomicReference<>();
            if (request.userId() != null) {
                userRef.set(userRepository.findById(request.userId()).orElse(null));
            }
            if (userRef.get() == null && request.email() != null && !request.email().isBlank()) {
                userRef.set(userRepository.findByEmail(request.email()).orElseGet(() -> userRepository.save(
                        User.builder()
                                .username(request.email().split("@")[0]
                                        + "-" + UUID.randomUUID().toString().substring(0, 4))
                                .email(request.email())
                                .password(UUID.randomUUID().toString())
                                .build())));
            }
            if (userRef.get() == null) {
                userRef.set(resolveUser(null));
            }
            User user = userRef.get();

            // User user = null;
            // if (request.userId() != null) {
            // user = userRepository.findById(request.userId()).orElse(null);
            // }
            // if (user == null && request.email() != null && !request.email().isBlank()) {
            // user = userRepository.findByEmail(request.email()).orElseGet(() ->
            // userRepository.save(
            // User.builder()
            // .username(request.email().split("@")[0])
            // .email(request.email())
            // .password(UUID.randomUUID().toString())
            // .build()
            // ));
            // }
            // if (user == null) {
            // user = resolveUser(null);
            // }

            PermissionRole role = parseRole(request.role());
            QuizPermission permission = quizPermissionRepository.findByQuiz_Id(quizId).stream()
                    .filter(existing -> existing.getUser() != null && existing.getUser().getId().equals(user.getId()))
                    .findFirst()
                    .orElseGet(QuizPermission::new);

            permission.setQuiz(quiz);
            permission.setUser(user);
            permission.setRole(role);
            permission.setStatus("INVITED");

            quizPermissionRepository.save(permission);
            return ResponseFactory.success(Map.of(
                    "quizId", quizId,
                    "userId", user.getId(),
                    "role", role,
                    "status", permission.getStatus()), "Invitation created successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to create invitation: " + e.getMessage(), 500);
        }
    }

    private QuizSummaryResponse toSummaryResponse(Quiz quiz) {
        int questionCount = quiz.getQuestions() == null ? 0 : quiz.getQuestions().size();
        int attempts = quiz.getQuizAttempts() == null ? 0 : quiz.getQuizAttempts().size();

        return new QuizSummaryResponse(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                attempts,
                difficultyFromQuestionCount(questionCount),
                questionCount,
                quiz.getPassingScore());
    }

    private QuizDetailResponse toDetailResponse(Quiz quiz) {
        List<QuizQuestionResponse> questions = questionRepository.findByQuiz_IdOrderById(quiz.getId()).stream()
                .map(this::toQuestionResponse)
                .toList();
        int questionCount = questions.size();
        int attempts = quiz.getQuizAttempts() == null ? 0 : quiz.getQuizAttempts().size();

        return new QuizDetailResponse(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getPassingScore(),
                quiz.getStatus() == null ? null : quiz.getStatus().name(),
                quiz.getPublicQuiz(),
                quiz.getCreatedBy() == null ? null : quiz.getCreatedBy().getId(),
                quiz.getCreatedBy() == null ? null : quiz.getCreatedBy().getUsername(),
                quiz.getShareToken(),
                quiz.getCreatedAt(),
                quiz.getExpiredAt(),
                questionCount,
                attempts,
                difficultyFromQuestionCount(questionCount),
                questions);
    }

    private QuizQuestionResponse toQuestionResponse(Question question) {
        List<QuizOptionResponse> options = question.getOptions().stream()
                .map(option -> new QuizOptionResponse(option.getId(), option.getContent(), option.getCorrect()))
                .toList();

        return new QuizQuestionResponse(
                question.getId(),
                question.getText(),
                question.getType() == null ? null : question.getType().name(),
                question.getExplanation(),
                options);
    }

    private QuizAttemptResponse toAttemptResponse(QuizAttempt attempt, int correctCount, int wrongCount, int total) {
        return new QuizAttemptResponse(
                attempt.getId(),
                attempt.getQuiz() == null ? null : attempt.getQuiz().getId(),
                attempt.getQuiz() == null ? null : attempt.getQuiz().getTitle(),
                attempt.getUser() == null ? null : attempt.getUser().getId(),
                attempt.getScore(),
                attempt.getPassed(),
                correctCount,
                wrongCount,
                total,
                attempt.getStartedAt(),
                attempt.getSubmittedAt());
    }

    private List<Question> mapQuestions(List<QuizQuestionRequest> requests, Quiz quiz) {
        return requests.stream().map(request -> {
            Question question = new Question();
            question.setId(request.id() == null || request.id().isBlank() ? generateQuestionId() : request.id());
            question.setQuiz(quiz);
            question.setText(request.text());
            question.setExplanation(request.explanation());
            question.setType(parseQuestionType(request.type()));

            if (request.options() != null) {
                question.getOptions().addAll(mapOptions(request.options(), question));
            }

            return question;
        }).toList();
    }

    // create import quiz from json file, if request contains id, try to find quiz
    // by id, if found update quiz, if not found create new quiz with given id, if
    // request does not contain id, create new quiz with generated id
    // stype MultipartFile as parameter, read file content as string, parse string
    // to QuizCreateRequest using ObjectMapper, then call
    // createQuiz(QuizCreateRequest) method to create or update quiz
    // {
    // "id": "quiz-python-intern",
    // "title": "Python Intern Level Quiz",
    // "description": "Practice Python basics to intermediate concepts",
    // "passingScore": 70,
    // "questions": [
    // {
    // "id": "q1",
    // "text": "Which data type is immutable in Python?",
    // "type": "multiple-choice",
    // "options": ["list", "dict", "set", "tuple"],
    // "correctAnswerIndex": 3,
    // "explanation": "Tuple is immutable."
    // },
    // {
    // "id": "q2",
    // "text": "What is the result of len('hello')?",
    // "type": "multiple-choice",
    // "options": ["4", "5", "6", "Error"],
    // "correctAnswerIndex": 1,
    // "explanation": "The string has 5 characters."
    // },

    @Transactional
    public ApiResponse<?> importQuiz(MultipartFile file) {
        try {
            String fileContent = new String(file.getBytes());
            QuizCreateRequest request = QuizCreateRequest.fromJson(fileContent);  
            return createQuiz(request);
        } catch (Exception e) {
            return ResponseFactory.error("Failed to import quiz: " + e.getMessage(), 500);
        }      

    }

    private List<QuizOption> mapOptions(List<QuizOptionRequest> requests, Question question) {
        return requests.stream().map(request -> {
            QuizOption option = new QuizOption();
            option.setId(request.id());
            option.setQuestion(question);
            option.setContent(request.content());
            option.setCorrect(request.correct() != null && request.correct());
            return option;
        }).toList();
    }

    private User resolveUser(Long userId) {
        if (userId != null) {
            return userRepository.findById(userId).orElseGet(() -> userRepository.save(User.builder()
                    .username("user-" + userId)
                    .email("user-" + userId + "@quizkot.local")
                    .password(UUID.randomUUID().toString())
                    .build()));
        }

        return userRepository.findAll().stream().findFirst().orElseGet(() -> userRepository.save(User.builder()
                .username("system")
                .email("system@quizkot.local")
                .password(UUID.randomUUID().toString())
                .build()));
    }

    private QuizStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return QuizStatus.DRAFT;
        }
        return QuizStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
    }

    private QuestionType parseQuestionType(String type) {
        if (type == null || type.isBlank()) {
            return QuestionType.MULTIPLE_CHOICE;
        }
        return QuestionType.valueOf(type.trim().toUpperCase(Locale.ROOT).replace('-', '_'));
    }

    private PermissionRole parseRole(String role) {
        if (role == null || role.isBlank()) {
            return PermissionRole.VIEWER;
        }
        return PermissionRole.valueOf(role.trim().toUpperCase(Locale.ROOT));
    }

    private String difficultyFromQuestionCount(int questionCount) {
        if (questionCount <= 5) {
            return "Easy";
        }
        if (questionCount <= 10) {
            return "Medium";
        }
        return "Hard";
    }

    private String generateQuizId() {
        return "quiz-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateQuestionId() {
        return "q-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String generateShareToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
