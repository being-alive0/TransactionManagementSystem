package com.example.transactionsystem.controller;

import com.example.transactionsystem.dto.TransactionResponse; 
import com.example.transactionsystem.model.Transaction;
import com.example.transactionsystem.model.enums.TransactionType;
import com.example.transactionsystem.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // This DTO (Data Transfer Object) is a clean way to accept request data
    public static class TransactionRequest {
        private Long walletId;
        private BigDecimal amount;
        private TransactionType type;
        private String idempotencyKey; // Field for idempotency

        // Getters and Setters
        public Long getWalletId() { return walletId; }
        public void setWalletId(Long walletId) { this.walletId = walletId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public TransactionType getType() { return type; }
        public void setType(TransactionType type) { this.type = type; }
        public String getIdempotencyKey() { return idempotencyKey; }
        public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    }

    // The method is updated to handle the idempotency key and new exceptions
 // Inside TransactionController.java...

    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest request) {
        try {
            // 1. Validate the idempotency key
            if (request.getIdempotencyKey() == null || request.getIdempotencyKey().isEmpty()) {
                return new ResponseEntity<>("Idempotency-Key is required", HttpStatus.BAD_REQUEST);
            }

            // 2. Pass the key to the service
            Transaction newTransaction = transactionService.createTransaction(
                    request.getWalletId(),
                    request.getAmount(),
                    request.getType(),
                    request.getIdempotencyKey()
            );

            // 3. --- THIS IS THE LINE TO FIX ---
            //    Add newTransaction.getIdempotencyKey() as the 6th argument
            TransactionResponse responseDto = new TransactionResponse(
                    newTransaction.getId(),
                    newTransaction.getWallet().getId(),
                    newTransaction.getType(),
                    newTransaction.getAmount(),
                    newTransaction.getStatus(),
                    newTransaction.getIdempotencyKey(), // <-- ADD THIS ARGUMENT
                    newTransaction.getCreatedAt()
            );

            // 4. Return the DTO
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        } catch (TransactionService.InsufficientFundsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TransactionService.DuplicateTransactionException e) { 
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}