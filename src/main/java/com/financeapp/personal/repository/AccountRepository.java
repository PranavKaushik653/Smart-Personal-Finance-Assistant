package com.financeapp.personal.repository;

import com.financeapp.personal.entity.Account;
import com.financeapp.personal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserOrderByAccountNameAsc(User user);

    List<Account> findByUserAndAccountTypeOrderByAccountNameAsc(User user, Account.AccountType accountType);

    @Query("SELECT COALESCE(SUM(a.currentBalance), 0) FROM Account a WHERE a.user = :user")
    BigDecimal calculateTotalBalanceForUser(User user);

    List<Account> findByUserAndCurrentBalanceGreaterThanOrderByCurrentBalanceDesc(User user, BigDecimal threshold);
}

