package com.financeapp.personal.controller;

import com.financeapp.personal.dto.AccountForm;
import com.financeapp.personal.entity.Account;
import com.financeapp.personal.entity.User;
import com.financeapp.personal.service.AccountService;
import com.financeapp.personal.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    @Autowired
    public AccountController(AccountService accountService,
                             UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }


    /**
     * LIST ALL ACCOUNTS
     */
    @GetMapping
    public String listAccounts(Model model) {

        User user = getTestUser();

        List<Account> accounts =
                accountService.findAccountsByUser(user);

        model.addAttribute("accounts", accounts);
        model.addAttribute("user", user);

        return "accounts/list";
    }



    /**
     * SHOW CREATE FORM
     */
    @GetMapping("/new")
    public String showNewAccountForm(Model model) {

        model.addAttribute("accountForm",
                new AccountForm());

        model.addAttribute("accountTypes",
                Account.AccountType.values());

        model.addAttribute("editing", false);   // FIXED (IMPORTANT)

        return "accounts/form";
    }



    /**
     * CREATE ACCOUNT
     */
    @PostMapping("/new")
    public String createAccount(
            @Valid @ModelAttribute("accountForm")
            AccountForm accountForm,

            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {


        if (result.hasErrors()) {

            model.addAttribute("accountTypes",
                    Account.AccountType.values());

            model.addAttribute("editing", false); // FIXED

            return "accounts/form";
        }


        try {

            Account account =
                    accountForm.toAccount();

            account.setUser(getTestUser());

            Account saved =
                    accountService.createAccount(account);


            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Account created successfully");

            return "redirect:/accounts";

        }
        catch (Exception e) {

            model.addAttribute("errorMessage",
                    e.getMessage());

            model.addAttribute("accountTypes",
                    Account.AccountType.values());

            model.addAttribute("editing", false); // FIXED

            return "accounts/form";
        }

    }



    /**
     * SHOW EDIT FORM
     */
    @GetMapping("/{id}/edit")
    public String showEditAccountForm(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Account> accountOpt =
                accountService.findById(id);


        if (accountOpt.isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Account not found");

            return "redirect:/accounts";
        }


        Account account =
                accountOpt.get();


        model.addAttribute("accountForm",
                new AccountForm(account));

        model.addAttribute("accountTypes",
                Account.AccountType.values());

        model.addAttribute("accountId", id);

        model.addAttribute("editing", true);

        return "accounts/form";
    }



    /**
     * UPDATE ACCOUNT
     */
    @PostMapping("/{id}/edit")
    public String updateAccount(
            @PathVariable Long id,

            @Valid
            @ModelAttribute("accountForm")
            AccountForm form,

            BindingResult result,

            Model model,

            RedirectAttributes redirectAttributes) {


        if (result.hasErrors()) {

            model.addAttribute("accountTypes",
                    Account.AccountType.values());

            model.addAttribute("accountId", id);

            model.addAttribute("editing", true);

            return "accounts/form";
        }



        Optional<Account> existingOpt =
                accountService.findById(id);


        if (existingOpt.isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Account not found");

            return "redirect:/accounts";
        }



        Account account =
                existingOpt.get();

        account.setAccountName(
                form.getAccountName());

        account.setAccountType(
                form.getAccountType());


        accountService.createAccount(account);


        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Account updated successfully");


        return "redirect:/accounts";
    }




    /**
     * TEST USER
     */
    private User getTestUser() {

        return userService
                .findByEmail("test@example.com")
                .orElseThrow(
                        () -> new RuntimeException(
                                "Test user not found"));
    }

}