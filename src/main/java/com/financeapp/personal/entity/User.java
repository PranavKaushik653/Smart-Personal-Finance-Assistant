package com.financeapp.personal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    // One user can have many accounts
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    // One user can have many budgets
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Budget> budgets = new ArrayList<>();

    // Custom helper methods remain
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public void addAccount(Account account) {
        accounts.add(account);
        account.setUser(this);
    }
    public void addBudget(Budget budget) {
        budgets.add(budget);
        budget.setUser(this);
    }
    public User(String lastName, String firstName,  String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', email='%s'}", id, getFullName(), email);
    }
}

