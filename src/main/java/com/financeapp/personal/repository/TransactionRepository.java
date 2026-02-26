package com.financeapp.personal.repository;

import com.financeapp.personal.entity.Account;
import com.financeapp.personal.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountOrderByTransactionDateDescCreatedAtDesc(Account account);

    List<Transaction> findByCategoryAndTransactionDateBetweenOrderByTransactionDateDesc(
            Transaction.Category category, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t JOIN t.account a " +
            "WHERE a.user.id = :userId AND t.category = :category " +
            "AND t.transactionType = 'EXPENSE' " +
            "AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    BigDecimal calculateSpendingByCategoryAndMonth(Long userId, Transaction.Category category, int year, int month);

    @Query("SELECT t FROM Transaction t JOIN t.account a WHERE a.user.id = :userId " +
            "ORDER BY t.transactionDate DESC, t.createdAt DESC")
    List<Transaction> findRecentTransactionsByUser(Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t JOIN t.account a " +
            "WHERE a.user.id = :userId AND t.transactionType = 'INCOME' " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalIncomeForUserInPeriod(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t JOIN t.account a " +
            "WHERE a.user.id = :userId AND t.transactionType = 'EXPENSE' " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal calculateTotalExpensesForUserInPeriod(Long userId, LocalDate startDate, LocalDate endDate);
}

