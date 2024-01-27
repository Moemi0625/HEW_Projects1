package com.example.webt.service;

import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.example.webt.entity.Account;
import com.example.webt.form.LoginData;
import com.example.webt.form.RegistData;
import com.example.webt.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoginService {
    private final AccountRepository accountRepository;

    // Loginチェック
    public boolean isValid(LoginData loginData, BindingResult result, Locale locale) {
        Optional<Account> account = accountRepository.findByLoginId(loginData.getLoginId());
        if (account.isEmpty() || !account.get().getPassword().equals(loginData.getPassword())) {
            // 登録されていないまたはパスワードが一致しない
            result.reject("loginError", "Invalid login credentials");
            return false;
        }

        return true;
    }
    
    // 登録画面用のチェック
    public boolean isValid(RegistData registData, BindingResult result, Locale locale) {
        if (!registData.getPassword1().equals(registData.getPassword2())) {
            // パスワード不一致
            result.reject("passwordError", "Passwords do not match");
            return false;
        }
        
        Optional<Account> account = accountRepository.findByLoginId(registData.getLoginId());
        if (account.isPresent()) {
            // 登録されている => 使われている
            result.reject("loginIdError", "Login ID is already in use");
            return false;
        }

        return true;
    }
}
