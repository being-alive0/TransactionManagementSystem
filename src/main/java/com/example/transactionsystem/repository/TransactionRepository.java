package com.example.transactionsystem.repository;

import com.example.transactionsystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // You can add methods to find transactions by wallet, etc.
	// Spring Data JPA will auto-generate the query for us
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
}