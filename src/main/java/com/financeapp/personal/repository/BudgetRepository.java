package com.financeapp.personal.repository;
import com.financeapp.personal.entity.Budget;
import com.financeapp.personal.entity.Transaction;
import com.financeapp.personal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserAndBudgetMonthOrderByCategoryAsc(User user, YearMonth budgetMonth);

    Optional<Budget> findByUserAndCategoryAndBudgetMonth(User user, Transaction.Category category, YearMonth budgetMonth);

    List<Budget> findByUserOrderByBudgetMonthDescCategoryAsc(User user);

    /**
     * Check if budget exists for user, category, and month
     */
    boolean existsByUserAndCategoryAndBudgetMonth(User user, Transaction.Category category, YearMonth budgetMonth);
}