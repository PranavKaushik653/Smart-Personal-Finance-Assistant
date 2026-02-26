package com.financeapp.personal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "budgets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "category", "budget_month"})
})
//@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category is required")
    @Column(nullable = false)
    private Transaction.Category category;

    @NotNull(message = "Budget amount is required")
    @DecimalMin(value = "0.01", message = "Budget amount must be greater than 0")
    @Column(name = "budget_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal budgetAmount;

    @NotNull(message = "Budget month is required")
    @Column(name = "budget_month", nullable = false)
    private YearMonth budgetMonth;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    public Budget() {
//        this.createdAt = LocalDateTime.now();
//    }

    /**
     * Constructor for creating budgets
     */
    public Budget(Transaction.Category category, BigDecimal budgetAmount, YearMonth budgetMonth, User user) {
        this();
        this.category = category;
        this.budgetAmount = budgetAmount;
        this.budgetMonth = budgetMonth;
        this.user = user;
    }

    public String getFormattedBudgetAmount() {
        return String.format("$%.2f", budgetAmount);
    }

    public double calculateUsagePercentage(BigDecimal spentAmount) {
        if (budgetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return spentAmount.divide(budgetAmount, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    public boolean isExceeded(BigDecimal spentAmount) {
        return spentAmount.compareTo(budgetAmount) > 0;
    }

    public BigDecimal getRemainingAmount(BigDecimal spentAmount) {
        return budgetAmount.subtract(spentAmount);
    }

    @Override
    public String toString() {
        return String.format("Budget{id=%d, category=%s, amount=%s, month=%s}",
                id, category, getFormattedBudgetAmount(), budgetMonth);
    }
}

