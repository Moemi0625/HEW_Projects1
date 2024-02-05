package com.example.webt.service;

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

    public boolean isValidLogin(LoginData loginData, BindingResult result) {
        Optional<Account> account = accountRepository.findByLoginId(loginData.getLoginId());
        if (account.isEmpty() || !account.get().getPassword().equals(loginData.getPassword())) {
            rejectError(result, "loginError", "Invalid login credentials");
            return false;
        }
        return true;
    }
    
    

    public boolean isValidRegistration(RegistData registData, BindingResult result) {
        validatePasswordMatch(registData.getPassword1(), registData.getPassword2(), result);
        validateLoginIdAvailability(registData.getLoginId(), result);
        return !result.hasErrors();
    }

    private void rejectError(BindingResult result, String errorCode, String defaultMessage) {
        result.rejectValue("", errorCode, defaultMessage);
    }

    private void validatePasswordMatch(String password1, String password2, BindingResult result) {
        if (!password1.equals(password2)) {
            rejectError(result, "passwordError", "Passwords do not match");
        }
    }

    private void validateLoginIdAvailability(String loginId, BindingResult result) {
        Optional<Account> account = accountRepository.findByLoginId(loginId);
        if (account.isPresent()) {
            rejectError(result, "loginIdError", "Login ID is already in use");
        }
    }
}