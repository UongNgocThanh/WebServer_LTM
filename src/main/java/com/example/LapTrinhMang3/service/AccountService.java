package com.example.LapTrinhMang3.service;

import com.example.LapTrinhMang3.AccountRepository;
import com.example.LapTrinhMang3.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("Email not found");
        }
        return new org.springframework.security.core.userdetails.User(
                account.getEmail(),
                account.getPassword(),
                Collections.emptyList()
        );
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account registerAccount(Account account) {
        account.setPassword(new BCryptPasswordEncoder().encode(account.getPassword())); // Mã hóa mật khẩu
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account accountDetails) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        existingAccount.setEmail(accountDetails.getEmail());
        existingAccount.setUsername(accountDetails.getUsername());
        if (accountDetails.getPassword() != null && !accountDetails.getPassword().isEmpty()) {
            existingAccount.setPassword(new BCryptPasswordEncoder().encode(accountDetails.getPassword()));
        }
        return accountRepository.save(existingAccount);
    }

    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found");
        }
        accountRepository.deleteById(id);
    }
}
