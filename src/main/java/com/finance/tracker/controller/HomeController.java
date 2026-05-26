package com.finance.tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "Finance Tracker API");
        response.put("status", "Running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().toString());

        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("health", "/health");
        endpoints.put("test", "/api/test/hello");
        endpoints.put("register", "POST /api/auth/register");
        endpoints.put("login", "POST /api/auth/login");
        endpoints.put("transactions", "GET /api/transactions (requires auth)");

        response.put("endpoints", endpoints);
        response.put("documentation", "https://github.com/YOUR_USERNAME/finance-tracker-backend");

        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("database", "Connected");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}