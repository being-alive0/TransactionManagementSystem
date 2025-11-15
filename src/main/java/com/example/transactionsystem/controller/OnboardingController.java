package com.example.transactionsystem.controller;

import com.example.transactionsystem.model.User;
import com.example.transactionsystem.service.OnboardingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnboardingController {

    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    // This DTO (Data Transfer Object) is a clean way to accept request data
    public static class UserCreationRequest {
        private String name;
        private String email;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    // This maps to your "POST /users" endpoint
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody UserCreationRequest request) {
        try {
            User newUser = onboardingService.createUserAndWallet(request.getName(), request.getEmail());
            // Return 201 Created status
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            // Basic error handling (e.g., if email is duplicate)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}