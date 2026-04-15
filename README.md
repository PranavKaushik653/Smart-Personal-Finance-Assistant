# Smart-Personal-Finance-Assistant
# Smart Personal Finance Assistant

A domain-rich personal finance management application built with Spring Boot, featuring multi-account tracking, categorized transaction management, and per-category budget enforcement with real-time analytics.

---

## What It Does

This application models the core data layer of a personal finance platform — tracking multiple bank accounts, logging income and expenses across 15 categories, and enforcing monthly budgets per category. The focus is on domain accuracy: credit card accounts are treated as liabilities in net worth calculations, budgets are scoped per user-category-month with a unique constraint, and all financial aggregations are written as custom JPQL queries rather than computed in application memory.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend Framework | Spring Boot 3.x |
| Data Layer | Spring Data JPA + Hibernate |
| Database | H2 (in-memory, dev) |
| Validation | Jakarta Bean Validation |
| Templating | Thymeleaf |
| Build Tool | Maven |
| Language | Java 17 |
| Utilities | Lombok |

---

## Domain Model

```
User
 └── Account (CHECKING / SAVINGS / CREDIT_CARD)
      └── Transaction (INCOME / EXPENSE)
           └── Category (15 types: SALARY, FREELANCE, GROCERIES, RENT_MORTGAGE, ...)

User
 └── Budget (per Category, per YearMonth)
```

**Key design decisions:**
- `Budget` has a `@UniqueConstraint` on `(user_id, category, budget_month)` — one budget per category per month per user, enforced at the DB level
- `Account.addTransaction()` updates `currentBalance` in real time based on transaction type
- `AccountService.calculateNetWorth()` subtracts credit card balances (liabilities) and adds checking/savings (assets)

---

## Core Features

### Account Management
- Create and manage multiple accounts: Checking, Savings, Credit Card
- Real-time balance updates on every transaction
- Net worth calculation with liability-aware logic (credit card debt is subtracted)

### Transaction Tracking
- 4 income categories: Salary, Freelance, Investment, Other Income
- 11 expense categories: Groceries, Dining Out, Transportation, Entertainment, Utilities, Rent/Mortgage, Healthcare, Shopping, Education, Travel, Other
- Signed formatted amounts (`+$500.00` for income, `-$120.00` for expense)

### Budget Management
- Set monthly spending limits per expense category
- Real-time budget usage percentage: `spentAmount / budgetAmount × 100`
- Overspend detection via `isExceeded(spentAmount)`
- Remaining budget calculation per category

### Analytics Queries (Custom JPQL)

```java
// Total spending by category in a given month
calculateSpendingByCategoryAndMonth(userId, category, year, month)

// Total income for a user in a date range
calculateTotalIncomeForUserInPeriod(userId, startDate, endDate)

// Total expenses for a user in a date range
calculateTotalExpensesForUserInPeriod(userId, startDate, endDate)

// All recent transactions across all accounts for a user
findRecentTransactionsByUser(userId)
```

---

## Project Structure

```
src/main/java/com/financeapp/personal/
├── controller/
│   ├── AccountController.java     # CRUD for accounts (list, create, edit)
│   └── HomeController.java        # Root route + test data bootstrap
├── dto/
│   ├── AccountForm.java           # Form binding DTO for account creation
│   └── TransactionForm.java       # Form binding DTO for transaction entry
├── entity/
│   ├── User.java                  # User domain model
│   ├── Account.java               # Account with balance logic
│   ├── Transaction.java           # Transaction with type + category enums
│   └── Budget.java                # Budget with enforcement methods
├── repository/
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   ├── TransactionRepository.java # Custom JPQL aggregation queries
│   └── BudgetRepository.java
└── service/
    ├── UserService.java            # User CRUD with email uniqueness check
    └── AccountService.java         # Account management + net worth logic
```

---

## Running Locally

**Prerequisites:** Java 17+, Maven 3.8+

```bash
# Clone the repository
git clone https://github.com/PranavKaushik653/Smart-Personal-Finance-Assistant.git
cd Smart-Personal-Finance-Assistant

# Run the application
./mvnw spring-boot:run

# Access the app
open http://localhost:8080

# H2 Console (inspect in-memory DB)
open http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:dcbapp | Username: sa | Password: password
```

---

## What I'd Add Next

- **Spring Security + JWT** — replace the hardcoded test user with a proper authentication layer
- **REST API layer** — expose all features as JSON endpoints alongside the Thymeleaf UI
- **PostgreSQL** — swap H2 for a persistent database with proper schema migrations (Flyway)
- **Monthly budget summary endpoint** — aggregate all categories for a given month into a single dashboard response (income, total spend, net, per-category breakdown vs budget)
- **Recurring transaction support** — model standing orders and salary credits

---

## Author

**Pranav Kaushik**
[GitHub](https://github.com/PranavKaushik653) · [LinkedIn](https://linkedin.com/in/pranav-kaushik-137a10266) · pranavkaushik653@gmail.com