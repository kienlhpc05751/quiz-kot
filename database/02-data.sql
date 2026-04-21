
-- =========================
-- SAMPLE DATA
-- =========================

-- USERS
INSERT INTO users (username, email, password)
VALUES 
('admin', 'admin@gmail.com', '$2a$10$fWg0hqpDC1nVNax6wzqts.TQhRZmC6ztLkpRk58zlaj7R.cAxb8UK'),
('user1', 'user1@gmail.com', '$2a$10$fWg0hqpDC1nVNax6wzqts.TQhRZmC6ztLkpRk58zlaj7R.cAxb8UK');

-- QUIZ
INSERT INTO quizzes (id, title, description, passing_score, created_by, is_public, status)
VALUES 
('quiz-1', 'General Knowledge', 'Test kiến thức tổng quát', 70, 1, TRUE, 'PUBLISHED');

-- QUESTION
INSERT INTO questions (id, quiz_id, text, type, explanation)
VALUES 
('q1', 'quiz-1', 'Thủ đô của Pháp là gì?', 'MULTIPLE_CHOICE', 'Paris là thủ đô của Pháp');

-- OPTIONS
INSERT INTO options (question_id, content, is_correct) VALUES
('q1', 'London', FALSE),
('q1', 'Berlin', FALSE),
('q1', 'Paris', TRUE),
('q1', 'Madrid', FALSE);

-- ATTEMPT
INSERT INTO quiz_attempts (user_id, quiz_id, score, is_pass, started_at, submitted_at)
VALUES (2, 'quiz-1', 100, TRUE, NOW(), NOW());

-- ANSWER
INSERT INTO user_answers (attempt_id, question_id, selected_option_id, is_correct)
VALUES (1, 'q1', 3, TRUE);
















INSERT INTO quizzes (id, title, description, passing_score, created_by, is_public, status)
VALUES 
('quiz-2', 'English Basics', 'Kiến thức tiếng Anh cơ bản', 60, 1, TRUE, 'PUBLISHED'),
('quiz-3', 'Math Fundamentals', 'Toán học cơ bản', 70, 1, TRUE, 'PUBLISHED');
-- QUESTIONS
INSERT INTO questions (id, quiz_id, text, type) VALUES
('q2', 'quiz-2', 'What is the plural of "child"?', 'MULTIPLE_CHOICE'),
('q3', 'quiz-2', 'Choose the correct verb: She ___ to school.', 'MULTIPLE_CHOICE'),
('q4', 'quiz-2', 'Which word is an adjective?', 'MULTIPLE_CHOICE'),
('q5', 'quiz-2', 'What is the past tense of "go"?', 'MULTIPLE_CHOICE'),
('q6', 'quiz-2', 'Which is a synonym of "big"?', 'MULTIPLE_CHOICE');

-- OPTIONS
INSERT INTO options (question_id, content, is_correct) VALUES
-- q2
('q2', 'childs', FALSE),
('q2', 'children', TRUE),
('q2', 'childes', FALSE),
('q2', 'childrens', FALSE),

-- q3
('q3', 'go', FALSE),
('q3', 'goes', TRUE),
('q3', 'going', FALSE),
('q3', 'gone', FALSE),

-- q4
('q4', 'run', FALSE),
('q4', 'happy', TRUE),
('q4', 'quickly', FALSE),
('q4', 'eat', FALSE),

-- q5
('q5', 'goed', FALSE),
('q5', 'went', TRUE),
('q5', 'gone', FALSE),
('q5', 'goes', FALSE),

-- q6
('q6', 'small', FALSE),
('q6', 'tiny', FALSE),
('q6', 'large', TRUE),
('q6', 'short', FALSE);

-- QUESTIONS
INSERT INTO questions (id, quiz_id, text, type) VALUES
('q7', 'quiz-3', '2 + 2 = ?', 'MULTIPLE_CHOICE'),
('q8', 'quiz-3', '5 * 6 = ?', 'MULTIPLE_CHOICE'),
('q9', 'quiz-3', '10 / 2 = ?', 'MULTIPLE_CHOICE'),
('q10', 'quiz-3', '9 - 3 = ?', 'MULTIPLE_CHOICE'),
('q11', 'quiz-3', 'Square root of 16?', 'MULTIPLE_CHOICE');

-- OPTIONS
INSERT INTO options (question_id, content, is_correct) VALUES
-- q7
('q7', '3', FALSE),
('q7', '4', TRUE),
('q7', '5', FALSE),
('q7', '6', FALSE),

-- q8
('q8', '30', TRUE),
('q8', '25', FALSE),
('q8', '35', FALSE),
('q8', '40', FALSE),

-- q9
('q9', '2', FALSE),
('q9', '3', FALSE),
('q9', '5', TRUE),
('q9', '10', FALSE),

-- q10
('q10', '5', FALSE),
('q10', '6', TRUE),
('q10', '7', FALSE),
('q10', '8', FALSE),

-- q11
('q11', '2', FALSE),
('q11', '4', TRUE),
('q11', '6', FALSE),
('q11', '8', FALSE);

-- ATTEMPT
INSERT INTO quiz_attempts (user_id, quiz_id, score, is_pass, started_at, submitted_at)
VALUES 
(2, 'quiz-2', 80, TRUE, NOW(), NOW()),
(2, 'quiz-3', 60, FALSE, NOW(), NOW());

-- ANSWERS (demo)
INSERT INTO user_answers (attempt_id, question_id, selected_option_id, is_correct) VALUES
-- quiz-2
(2, 'q2', 6, TRUE),
(2, 'q3', 10, TRUE),

-- quiz-3
(3, 'q7', 21, TRUE),
(3, 'q8', 25, TRUE);