package com.financeapp.personal.dto;

import com.financeapp.personal.entity.Transaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionForm {
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Please select transaction type")
    private Transaction.TransactionType transactionType;

    @NotNull(message = "Please select a category")
    private Transaction.Category category;

    @NotNull(message = "Transaction date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    @NotNull(message = "Please select an account")
    private Long accountId;

    // Default constructor
    public TransactionForm() {
        // Default to today's date
        this.transactionDate = LocalDate.now();
    }

    // Constructor for editing existing transactions
    public TransactionForm(Transaction transaction) {
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
        this.transactionType = transaction.getTransactionType();
        this.category = transaction.getCategory();
        this.transactionDate = transaction.getTransactionDate();
        this.accountId = transaction.getAccount().getId();
    }


}
