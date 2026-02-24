package com.financeapp.personal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description is required")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Transaction type is required")
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category is required")
    @Column(nullable = false)
    private Category category;

    @NotNull(message = "Transaction date is required")
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public enum TransactionType {
        INCOME("Income"),
        EXPENSE("Expense");

        private final String displayName;
        TransactionType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum Category {
        // Income categories
        SALARY("Salary"),
        FREELANCE("Freelance"),
        INVESTMENT("Investment"),
        OTHER_INCOME("Other Income"),

        // Expense categories
        GROCERIES("Groceries"),
        DINING_OUT("Dining Out"),
        TRANSPORTATION("Transportation"),
        ENTERTAINMENT("Entertainment"),
        UTILITIES("Utilities"),
        RENT_MORTGAGE("Rent/Mortgage"),
        HEALTHCARE("Healthcare"),
        SHOPPING("Shopping"),
        EDUCATION("Education"),
        TRAVEL("Travel"),
        OTHER_EXPENSE("Other Expense");

        private final String displayName;
        Category(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }

        public static Category[] getIncomeCategories() {
            return new Category[]{SALARY, FREELANCE, INVESTMENT, OTHER_INCOME};
        }

        public static Category[] getExpenseCategories() {
            return new Category[]{GROCERIES, DINING_OUT, TRANSPORTATION, ENTERTAINMENT,
                    UTILITIES, RENT_MORTGAGE, HEALTHCARE, SHOPPING,
                    EDUCATION, TRAVEL, OTHER_EXPENSE};
        }
    }

    public String getFormattedAmount() {
        return String.format("$%.2f", amount);
    }

    public String getSignedFormattedAmount() {
        String prefix = transactionType == TransactionType.INCOME ? "+" : "-";
        return prefix + getFormattedAmount();
    }
}
