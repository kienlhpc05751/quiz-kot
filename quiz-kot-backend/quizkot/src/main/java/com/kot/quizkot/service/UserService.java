package com.kot.quizkot.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kot.quizkot.dto.response.ApiResponse;
import com.kot.quizkot.dto.response.UserResponse;
import com.kot.quizkot.entity.User;
import com.kot.quizkot.repository.UserRepository;
import com.kot.quizkot.util.ResponseFactory;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public ApiResponse<?> getAllUsers() {
        try {
            List<UserResponse> users = userRepository.findAll().stream()
                    .map(this::toUserResponse)
                    .distinct()
                    .toList();
            return ResponseFactory.success(users, "Users retrieved successfully");
        } catch (Exception e) {
            return ResponseFactory.error("Failed to retrieve users: " + e.getMessage(), 500);
        }
    }


    public ApiResponse<?> getUserById(Long id) {
        try {
            return userRepository.findById(id)
                    .<ApiResponse<?>>map(user -> ResponseFactory.success(toUserResponse(user), "User retrieved successfully"))
                    .orElseGet(() -> ResponseFactory.error("User not found", 404));
        } catch (Exception e) {
            return ResponseFactory.error("Failed to retrieve user: " + e.getMessage(), 500);
        }
    }

    public User findOrCreateSystemUser() {
        return userRepository.findAll().stream().findFirst().orElseGet(() -> {
            User user = User.builder()
                    .username("system")
                    .email("system@quizkot.local")
                    .password("system")
                    .build();
            return userRepository.save(user);
        });
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

}
