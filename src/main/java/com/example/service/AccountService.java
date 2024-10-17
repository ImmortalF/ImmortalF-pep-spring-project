package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.InvalidCredentialsException;
import com.example.exception.MessageValidationException;
import com.example.exception.UsernameAlreadyExistsException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) {

        if (account.getUsername() == null || account.getUsername().isEmpty()) {
            throw new MessageValidationException("Username cannot be blank.");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new MessageValidationException("Password must be at least 4 characters long.");
        }
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }

        return accountRepository.save(account);
    }

    public Account login(Account account) {
        Account existingAccount = accountRepository.findByUsernameAndPassword(account.getUsername(),
                account.getPassword());

        if (existingAccount == null) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        return existingAccount;
    }

}
