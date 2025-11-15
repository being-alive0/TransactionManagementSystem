package com.example.transactionsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transactionsystem.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA automatically creates a query from this method name
    Optional<User> findByEmail(String email);
}