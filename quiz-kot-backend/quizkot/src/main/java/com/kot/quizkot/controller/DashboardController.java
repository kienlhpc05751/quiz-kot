package com.kot.quizkot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kot.quizkot.dto.response.ApiResponse;
import com.kot.quizkot.service.DashboardService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse summary() {
        return dashboardService.getSummary();
    }

    @GetMapping("/activity")
    public ApiResponse activity() {
        return dashboardService.getActivity();
    }
}