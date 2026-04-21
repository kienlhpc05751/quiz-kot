package com.kot.quizkot.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kot.quizkot.dto.request.AuthLoginRequest;
import com.kot.quizkot.dto.request.AuthRegisterRequest;
import com.kot.quizkot.dto.response.ApiResponse;
import com.kot.quizkot.dto.response.AuthResponse;
import com.kot.quizkot.dto.response.UserResponse;
import com.kot.quizkot.entity.User;
import com.kot.quizkot.repository.UserRepository;
import com.kot.quizkot.util.ResponseFactory;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApiResponse register(AuthRegisterRequest request) {
        try {
            if (request.email() == null || request.email().isBlank()) {
                return ResponseFactory.error("Email is required", 400);
            }

            if (userRepository.existsByEmail(request.email())) {
                return ResponseFactory.error("Email already exists", 409);
            }

            String username = request.name();
            if (username == null || username.isBlank()) {
                username = request.email().split("@")[0];
            }

            User user = User.builder()
                    .username(username)
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .build();

            User savedUser = userRepository.save(user);
            String token = UUID.randomUUID().toString();
            return ResponseFactory.success(new AuthResponse(token, token, toUserResponse(savedUser)), "Registration successful");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to register: " + e.getMessage(), 500);
        }
    }

    public ApiResponse login(AuthLoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.email())
                    .orElse(null);

            if (user == null) {
                return ResponseFactory.error("Invalid email or password", 401);
            }

            if (!passwordEncoder.matches(request.password(), user.getPassword())) {
                return ResponseFactory.error("Invalid email or password", 401);
            }

            String token = UUID.randomUUID().toString();
            return ResponseFactory.success(new AuthResponse(token, token, toUserResponse(user)), "Login successful");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to login: " + e.getMessage(), 500);
        }
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}