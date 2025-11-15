package com.example.transactionsystem.controller;

import com.example.transactionsystem.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;

@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // This maps to your "GET /wallets/{walletId}/balance" endpoint
    @GetMapping("/wallets/{walletId}/balance")
    public ResponseEntity<BalanceResponse> getWalletBalance(@PathVariable Long walletId) {
        try {
            BigDecimal balance = walletService.getBalance(walletId);
            return ResponseEntity.ok(new BalanceResponse(balance));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // A simple DTO class for a clean JSON response
    private static class BalanceResponse {
        private final BigDecimal balance;

        public BalanceResponse(BigDecimal balance) {
            this.balance = balance;
        }

        public BigDecimal getBalance() {
            return balance;
        }
    }
}