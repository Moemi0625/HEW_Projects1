package com.example.webt.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webt.entity.Account;
import com.example.webt.form.LoginData;
import com.example.webt.form.RegistData;
import com.example.webt.repository.AccountRepository;
import com.example.webt.service.LoginService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final AccountRepository accountRepository;
    private final LoginService loginService;
    private final HttpSession session;

    @GetMapping("/home")
    public String home() {
        return "home";
    }
    
    @GetMapping("/new")
    public String showNewPage() {
        return "new";
    }
    
    @GetMapping("/contactForm")
    public String showContactForm() {
        return "contactForm";
    }
    
    // Login画面表示
    @GetMapping("/")
    public ModelAndView showLogin(ModelAndView mv) {
        mv.setViewName("loginForm");
        mv.addObject("loginData", new LoginData());
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView login(ModelAndView mv) {
        mv.setViewName("loginForm");
        mv.addObject("loginData", new LoginData());
        return mv;
    }

    @PostMapping("/login/do")
    public String login(@ModelAttribute @Validated LoginData loginData,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        // バリデーション
        if (result.hasErrors() || !loginService.isValid(loginData, result, Locale.getDefault())) {
            // エラーメッセージをログに出力
            result.getAllErrors().forEach(error -> {
                System.out.println(error.getDefaultMessage());
            });

            // エラーがある場合、エラーメッセージを画面に表示する
            model.addAttribute("error", "入力内容が正しくありません。");

            return "loginForm";
        }

        // LoginしたユーザーのaccountIdをセッションへ格納する
        Account account = accountRepository.findByLoginId(loginData.getLoginId()).get();
        session.setAttribute("accountId", account.getId());

        // Login成功時のリダイレクト(マイページに遷移)
        return "redirect:/mypage";
    }

    
    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession session) {
        // Add session attributes to the model
        Object accountId = session.getAttribute("accountId");

        if (accountId != null) {
            // 正常にaccountIdがセッションに設定されている場合
            model.addAttribute("accountId", accountId);
            System.out.println("myPage: Account ID - " + accountId);

            // Return the template name
            return "mypage";
        } else {
            // セッションにaccountIdが設定されていない場合
            System.out.println("myPage: Account ID not found in session.");
            // またはログインが必要な場合には、ログイン画面にリダイレクトするなどの処理を追加できます。
            return "redirect:/login";
        }
    }

    
    // Logout処理
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        // セッション情報をクリアする
        session.invalidate();

        // Logout完了時のリダイレクト
        return "redirect:/";
    }

    // ユーザー新規登録 - 画面表示
    @GetMapping("/regist")
    public ModelAndView showRegist(ModelAndView mv) {
        mv.setViewName("registForm");
        mv.addObject("registData", new RegistData());
        return mv;
    }

    @PostMapping("/regist/do")
    public String registNewUser(@ModelAttribute @Validated RegistData registData,
                                 BindingResult result, 
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
    	
    	if (!result.hasErrors() && loginService.isValid(registData, result, Locale.getDefault())) {
            // エラーなし -> 登録
            Account account = registData.toEntity();
            accountRepository.saveAndFlush(account);

            // 登録完了時のリダイレクト
            return "redirect:/";

        } else {
            // エラーあり -> 登録画面に戻る
            return "registForm";
        }
    }

    // ユーザー新規登録 - 戻る
    @GetMapping("/regist/cancel")
    public String registCancel() {
        return "redirect:/";
    }
}
