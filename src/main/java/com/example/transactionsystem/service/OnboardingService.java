package com.example.transactionsystem.service;

import com.example.transactionsystem.model.User;
import com.example.transactionsystem.model.Wallet;
import com.example.transactionsystem.repository.UserRepository;
import com.example.transactionsystem.repository.WalletRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import this!

@Service
public class OnboardingService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    // Spring injects the repositories for us (Dependency Injection)
    public OnboardingService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    /**
     * Creates a new User and an associated Wallet in a single database transaction.
     */
    @Transactional // This is key! If *any* part fails, it all rolls back.
    public User createUserAndWallet(String name, String email) {
        
        // 1. Create the user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        User savedUser = userRepository.save(user);

        // 2. Create the wallet and link it to the user
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        // The balance is set to 0 by default (@PrePersist in Wallet.java)
        walletRepository.save(wallet);

        return savedUser;
    }
}