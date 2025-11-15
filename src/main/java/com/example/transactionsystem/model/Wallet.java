package com.example.transactionsystem.model;

import jakarta.persistence.*;
import java.math.BigDecimal; // IMPORTANT: Always use BigDecimal for money
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This creates the 'user_id' foreign key relationship
    @OneToOne(fetch = FetchType.LAZY) // Lazy fetch is usually more efficient
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // Use BigDecimal for currency to avoid rounding errors
    @Column(nullable = false)
    private BigDecimal balance;

    // This is for your optimistic locking strategy!
    @Version
    private Long version; 

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        // Set default balance
        this.balance = BigDecimal.ZERO; 
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    // (Generate these in your IDE)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}