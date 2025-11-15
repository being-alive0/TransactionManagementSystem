package com.example.transactionsystem.service;

import com.example.transactionsystem.model.Wallet;
import com.example.transactionsystem.repository.WalletRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Gets the balance for a wallet.
     * This method is cached by Spring.
     */
    // This annotation is the magic!
    @Cacheable(value = "wallet-balance", key = "#walletId")
    public BigDecimal getBalance(Long walletId) {
        // This log will only print on a "cache miss"
        System.out.println("--- FETCHING FROM DATABASE ---");
        
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        
        return wallet.getBalance();
    }
}