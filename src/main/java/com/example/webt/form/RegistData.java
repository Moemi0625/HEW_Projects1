package com.example.webt.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.example.webt.entity.Account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistData {

    @NotBlank(message = "名前は必須項目です")
    private String name;

    @NotBlank(message = "ログインIDは必須項目です")
    @Length(min=8, max=16, message = "ログインIDは8～16文字で入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "ユーザIDは半角英数字のみ使用できます")
    private String loginId;

    @NotBlank(message = "パスワードは必須項目です")
    @Length(min=8, max=16, message = "パスワードは8～16文字で入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "パスワードは半角英数字のみ使用できます")
    private String password1;

    @NotBlank(message = "パスワード（確認用）は必須項目です")
    @Length(min=8, max=16, message = "パスワードは8～16文字で入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "パスワードは半角英数字のみ使用できます")
    private String password2;

    private MultipartFile imageFile;

    public Account toEntity() {
        Account account = new Account();
        account.setName(this.name);
        account.setLoginId(this.loginId);
        account.setPassword(this.password1);

        if (imageFile != null) {
            account.setImagePath(imageFile.getOriginalFilename());
        }

        return account;
    }
}
