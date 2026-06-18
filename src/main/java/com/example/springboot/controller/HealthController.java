package com.example.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Application is running successfully");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Spring Boot OpenShift Application");
        response.put("version", "1.0.0");
        response.put("description", "Simple Spring Boot Application for OpenShift");
        response.put("author", "Your Name");
        return response;
    }
}
