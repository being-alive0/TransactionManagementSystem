package com.example.transactionsystem.dto;

import com.example.transactionsystem.model.enums.TransactionStatus;
import com.example.transactionsystem.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// This is a simple "POJO" (Plain Old Java Object)
// It only has getters and the data we want to show.
public class TransactionResponse {

    private Long id;
    private Long walletId; // We flatten the object to just the ID
    private TransactionType type;
    private BigDecimal amount;
    private TransactionStatus status;
    private String idempotencyKey; // <-- ADD THIS
    private LocalDateTime createdAt;

    // A constructor is a clean way to create the DTO from the Entity
    public TransactionResponse(Long id, Long walletId, TransactionType type, BigDecimal amount, TransactionStatus status, String idempotencyKey,LocalDateTime createdAt) {
        this.id = id;
        this.walletId = walletId;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.idempotencyKey = idempotencyKey; // <-- ADD THIS
        this.createdAt = createdAt;
    }

    // --- Getters Only ---
    
    public Long getId() {
        return id;
    }

    public Long getWalletId() {
        return walletId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }
    public String getIdempotencyKey() { return idempotencyKey; }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}