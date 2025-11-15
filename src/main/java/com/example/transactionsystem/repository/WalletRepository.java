package com.example.transactionsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transactionsystem.model.Wallet;
import jakarta.persistence.LockModeType; // Import this
import org.springframework.data.jpa.repository.Lock; // Import this
import org.springframework.data.jpa.repository.Query; // Import this
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * Finds a wallet by its ID and applies a
     * PESSIMISTIC_WRITE lock (database-level "FOR UPDATE")
     * to prevent concurrent modifications.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    Optional<Wallet> findByIdForUpdate(Long id);
}



