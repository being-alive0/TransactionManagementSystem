package com.example.transactionsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // This maps the class to the "users" table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id;

    private String name;

    @Column(unique = true, nullable = false) // Emails should be unique and not null
    private String email;

    @Column(name = "created_at", updatable = false) // Match your DB column
    private LocalDateTime createdAt;

    // This method automatically sets the creation timestamp 
    // before a new user is saved
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    // You'll need these for Spring to work with the object.
    // Your IDE can generate them for you (e.g., Alt+Insert in IntelliJ)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}