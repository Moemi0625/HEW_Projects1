package com.example.webt.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webt.config.Config;
import com.example.webt.entity.Account;
import com.example.webt.form.LoginData;
import com.example.webt.form.RegistData;
import com.example.webt.repository.AccountRepository;
import com.example.webt.service.LoginService;

import jakarta.servlet.http.HttpServletRequest;
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
        if (result.hasErrors() || !loginService.isValidLogin(loginData, result)) {
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

    @GetMapping("/logout")
    public String logoutGet(HttpServletRequest request) {
        return logout(request);
    }

    @PostMapping("/logout")
    public String logoutPost(HttpServletRequest request) {
        return logout(request);
    }

    private String logout(HttpServletRequest request) {
        // ログアウト処理...
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // セッションを無効化
        }
        return "redirect:/login";
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
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {

        if (!result.hasErrors() && loginService.isValidRegistration(registData, result)) {
            try {
                // エラーなし -> トランザクション内でアカウントと画像を保存
                Account account = registData.toEntity();

                // アカウントの保存
                Account savedAccount = accountRepository.saveAndFlush(account);

                // 画像の保存
                String imagePath = saveImage(imageFile);
                savedAccount.setImagePath("/images/" + imagePath); // /images/ を含めて保存
                accountRepository.save(savedAccount);

                // 登録完了時のリダイレクト
                return "redirect:/mypage";

            } catch (Exception e) {
                // エラーが発生した場合、適切なエラーメッセージをリダイレクト先に伝える
                redirectAttributes.addFlashAttribute("error", "アカウントの作成中にエラーが発生しました。");
                return "redirect:/regist";
            }
        } else {
            // エラーあり -> 登録画面に戻る
            return "registForm";
        }
    }

    @PostMapping("/user/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   HttpSession session) {
        // 画像を保存し、保存されたファイルのパスを取得
        String imagePath = saveImage(file);
        // imagePath をセッションに保存（次のリクエストで再利用されないように）
        session.setAttribute("tempImagePath", "/images/" + imagePath);
        return "redirect:/regist/do";
    }

    private String saveImage(MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                String imagePath = Config.IMAGE_PATH + fileName;
                Files.copy(imageFile.getInputStream(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null; // 画像がアップロードされていない場合は null を返す
    }

    // ユーザー新規登録 - 戻る
    @GetMapping("/regist/cancel")
    public String registCancel() {
        return "redirect:/";
    }
    
    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession session) {
        // Add session attributes to the model
        Object accountId = session.getAttribute("accountId");

        if (accountId != null) {
            // 正常にaccountIdがセッションに設定されている場合
            model.addAttribute("accountId", accountId);

            // ユーザーのアカウント情報を取得してモデルに追加
            Account account = accountRepository.findById((Integer) accountId).orElse(null);
            if (account != null) {
                model.addAttribute("user", account);
            }

            // Return the template name
            return "mypage";
        } else {
            // セッションにaccountIdが設定されていない場合
            System.out.println("myPage: Account ID not found in session.");
            // またはログインが必要な場合には、ログイン画面にリダイレクトするなどの処理を追加できます。
            return "redirect:/login";
        }
    }
    
    // Add this method in your controller
    @GetMapping("/user/list")
    public String showUserList(Model model) {
        List<Account> userList = accountRepository.findAll();
        model.addAttribute("userList", userList);
        return "userList";
    }


}
