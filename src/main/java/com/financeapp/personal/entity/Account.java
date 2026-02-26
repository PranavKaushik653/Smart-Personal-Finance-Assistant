package com.financeapp.personal.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account name is required")
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Account type is required")
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @DecimalMin(value = "0.0", inclusive = false, message = "Initial balance must be positive")
    @Column(name = "initial_balance", precision = 10, scale = 2)
    private BigDecimal initialBalance;

    @Column(name = "current_balance", precision = 10, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Many accounts belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // One account can have many transactions
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    public enum AccountType {
        CHECKING("Checking Account"),
        SAVINGS("Savings Account"),
        CREDIT_CARD("Credit Card");

        private final String displayName;

        AccountType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public Account(String accountName, AccountType accountType, BigDecimal initialBalance, User user) {
        this();
        this.accountName = accountName;
        this.accountType = accountType;
        this.initialBalance = initialBalance;
        this.currentBalance = initialBalance;
        this.user = user;

    }

    /**
     * Helper method to add a transaction and update balance
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);

        // Update current balance based on transaction type
        if (transaction.getTransactionType() == Transaction.TransactionType.INCOME) {
            this.currentBalance = this.currentBalance.add(transaction.getAmount());
        } else {
            this.currentBalance = this.currentBalance.subtract(transaction.getAmount());
        }
    }

    /**
     * Get formatted balance for display
     */
    public String getFormattedCurrentBalance() {
        return String.format("$%.2f", currentBalance);
    }

    /**
     * Check if account is a credit card (affects balance calculations)
     */
    public boolean isCreditCard() {
        return accountType == AccountType.CREDIT_CARD;
    }


}
