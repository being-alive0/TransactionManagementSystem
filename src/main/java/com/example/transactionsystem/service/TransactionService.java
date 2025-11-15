package com.example.transactionsystem.service;

import com.example.transactionsystem.model.Transaction;
import com.example.transactionsystem.model.Wallet;
import com.example.transactionsystem.model.enums.TransactionStatus;
import com.example.transactionsystem.model.enums.TransactionType;
import com.example.transactionsystem.repository.TransactionRepository;
import com.example.transactionsystem.repository.WalletRepository;
import org.springframework.cache.annotation.CacheEvict; // Import @CacheEvict
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Important!
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    // Custom Exception for insufficient funds
    public static class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    // Custom Exception for duplicate requests
    public static class DuplicateTransactionException extends RuntimeException {
        public DuplicateTransactionException(String message) {
            super(message);
        }
    }
    
    @Transactional
    @CacheEvict(value = "wallet-balance", key = "#walletId")
    public Transaction createTransaction(Long walletId, BigDecimal amount, TransactionType type, String idempotencyKey) {
        
        // --- IDEMPOTENCY CHECK ---
        // Check if a transaction with this key already exists *before* doing any work.
        // This is fast and doesn't require a lock.
        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(idempotencyKey);
        
        if (existingTransaction.isPresent()) {
            // A request with this key has been processed.
            // Throw an exception to be handled by the controller.
            throw new DuplicateTransactionException("Duplicate transaction with key: " + idempotencyKey);
        }
        // --- END IDEMPOTENCY CHECK ---

        // 1. Lock the wallet row to prevent concurrent updates
        Wallet wallet = walletRepository.findByIdForUpdate(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // 2. Create the transaction record
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setIdempotencyKey(idempotencyKey); // <-- SET THE KEY

        // 3. Apply business logic
        if (type == TransactionType.DEBIT) {
            // Check for sufficient funds
            if (wallet.getBalance().compareTo(amount) < 0) {
                transaction.setStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction); // Save the FAILED record
                // Throwing this exception triggers the rollback
                throw new InsufficientFundsException("Insufficient funds in wallet " + walletId);
            }
            // Apply debit
            wallet.setBalance(wallet.getBalance().subtract(amount));
        } else if (type == TransactionType.CREDIT) {
            // Apply credit
            wallet.setBalance(wallet.getBalance().add(amount));
        }

        // 4. Update wallet balance
        walletRepository.save(wallet);

        // 5. Set transaction as completed and save it
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }
}