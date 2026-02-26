package com.financeapp.personal.dto;

import com.financeapp.personal.entity.Account;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountForm {

    @NotBlank(message = "Account name is reuquired")
    private String accountName;

    @NotNull(message = "Please select account type")
    private Account.AccountType accountType;

    @NotNull(message = "Intial balance is required")
    @DecimalMin(value = "0.0", message = "Initial balance cannot be neegative")
    private BigDecimal initialBalance;

    public AccountForm() {}

    public AccountForm(Account account) {
        this.accountName = account.getAccountName();
        this.accountType = account.getAccountType();
        this.initialBalance = account.getInitialBalance();
    }

    public Account toAccount() {
        Account account = new Account();
        account.setAccountName(this.accountName);
        account.setAccountType(this.accountType);
        account.setInitialBalance(this.initialBalance);
        account.setCurrentBalance(this.initialBalance);
        return account;
    }

}
