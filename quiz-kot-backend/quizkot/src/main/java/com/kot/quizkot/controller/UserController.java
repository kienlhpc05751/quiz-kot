package com.kot.quizkot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.kot.quizkot.service.UserService;
import com.kot.quizkot.dto.response.ApiResponse;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ApiResponse getAllUsers() {
        System.out.println("UserController: getAllUsers called");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ApiResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
