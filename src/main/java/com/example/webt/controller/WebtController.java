package com.example.webt.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webt.dao.WebtDaoImpl;
import com.example.webt.entity.Webt;
import com.example.webt.form.WebtData;
import com.example.webt.form.WebtQuery;
import com.example.webt.repository.WebtRepository;
import com.example.webt.service.WebtService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class WebtController {
    private final WebtRepository webtRepository;
    private final WebtService webtService;
    private final HttpSession session;
    private final WebtDaoImpl webtDaoImpl;

    @GetMapping("/webt")
    public ModelAndView showWebtList(ModelAndView mv,
    		@PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable) {
        mv.setViewName("webtList");
        
        Page<Webt> webtPage = webtRepository.findAll(pageable);
        
        mv.addObject("webtQuery", new WebtQuery());
        mv.addObject("webtPage", webtPage); // ②
		mv.addObject("webtList", webtPage.getContent()); 
		session.setAttribute("webtQuery", new WebtQuery()); // ④
        return mv;
    }
    
   
    @PostMapping("/webt/query")
	public ModelAndView queryWebt(@ModelAttribute WebtQuery webtQuery, BindingResult result,
			@PageableDefault(page = 0, size = 5) Pageable pageable, // ①

			ModelAndView mv) {
		mv.setViewName("webtList");
		Page<Webt> webtPage = null; // ②
		if (webtService.isValid(webtQuery, result)) {
			// エラーがなければ検索
			webtPage = webtDaoImpl.findByCriteria(webtQuery, pageable); // ③
			// 入力された検索条件を session に保存
			session.setAttribute("webtQuery", webtQuery); // ④
			mv.addObject("webtPage", webtPage); // ⑤
			mv.addObject("webtList", webtPage.getContent()); // ⑥
		} else {
			// エラーがあった場合検索
			mv.addObject("webtPage", null); // ⑤’
			mv.addObject("webtList", null); // ⑥’
		}
		return mv;
	}
    
  
    @GetMapping("/webt/query")
    public ModelAndView queryWebt(@PageableDefault(page = 0, size = 5) Pageable pageable, ModelAndView mv) {
        mv.setViewName("webtList");
        // session に保存されている条件で検索
        WebtQuery webtQuery = (WebtQuery) session.getAttribute("webtQuery");
        Page<Webt> webtPage = webtDaoImpl.findByCriteria(webtQuery, pageable);
        mv.addObject("webtQuery", webtQuery); // 検索条件表示用
        mv.addObject("webtPage", webtPage); // page 情報
        mv.addObject("webtList", webtPage.getContent()); // 検索結果
        return mv;
    }     

    @GetMapping("/webt/create")
    public ModelAndView createWebt(ModelAndView mv) {
        mv.setViewName("webtForm");
        mv.addObject("webtData", new WebtData());
        session.setAttribute("mode", "create"); // ③
        return mv;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        // 画像を保存し、保存されたファイルのパスを取得
        String imagePath = saveImage(file);
        // 画像の保存パスをセッションに保存
        session.setAttribute("imagePath", imagePath);
        return "redirect:/webt/create";
    }

    
    @PostMapping("/webt/cancel")
	public String cancel() {
		return "redirect:/webt";
	}


    @PostMapping("/webt/create")
    public ModelAndView createWebt(
            @ModelAttribute @Validated WebtData webtData,
            BindingResult result,
            ModelAndView mv,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {

        // エラーチェック
        boolean isValid = webtService.isValid(webtData, result);
        if (!result.hasErrors() && isValid) {
            // エラーなし
            Webt webt = webtData.toEntity();

            // If it's a new entry, set a default rating of 3
            if (webtData.getRating() == null) {
                webt.setRating(3);
            }

            // 画像を保存
            String imagePath = saveImage(imageFile);
            webt.setImagePath(imagePath);

            // データベースに保存
            webtRepository.saveAndFlush(webt);

            // リダイレクトして PRG パターンを適用
            redirectAttributes.addFlashAttribute("successMessage", "新規登録が完了しました。");
            return new ModelAndView("redirect:/webt");
        } else {
            // エラーあり
            mv.setViewName("webtForm");
            mv.addObject("webtData", webtData);
            return mv;
        }
    }

    private String saveImage(MultipartFile imageFile) {
        // 画像を保存するロジックを実装
        try {
            String imagePath = "images/" + UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Files.copy(imageFile.getInputStream(), Paths.get(imagePath), StandardCopyOption.REPLACE_EXISTING);
            return imagePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/webt/{id}")
	public ModelAndView webtById(@PathVariable(name = "id") int id, ModelAndView mv) {
		mv.setViewName("webtForm");
		Webt webt = webtRepository.findById(id).get(); // ①
		mv.addObject("webtData", webt); // ※ b
		session.setAttribute("mode", "update"); // ②
		return mv;
	}

	@PostMapping("/webt/update")
	public String updateWebt(@ModelAttribute @Validated WebtData webtData, BindingResult result, Model model) {
		// エラーチェック
		boolean isValid = webtService.isValid(webtData, result);
		if (!result.hasErrors() && isValid) {
			// エラーなし
			Webt webt = webtData.toEntity();
			webtRepository.saveAndFlush(webt); // ①
			return "redirect:/webt";
		} else {
			// エラーあり
			return "webtForm";
		}
	}

	@PostMapping("/webt/delete")
	public String deleteWebt(@ModelAttribute WebtData webtData) {
		webtRepository.deleteById(webtData.getId());
		return "redirect:/webt";
	}
	
	@GetMapping("/webt/details/{id}")
	public String showWebtDetails(@PathVariable(name = "id") String id, Model model) {
	    if ("details".equals(id)) {
	        return "redirect:/error"; 
	    }

	    try {
	        int webtId = Integer.parseInt(id);
	        Webt webt = webtRepository.findById(webtId).orElse(null);

	        if (webt == null) {
	            return "redirect:/error"; 
	        }

	        model.addAttribute("webt", webt);
	        return "webtDetails";
	    } catch (NumberFormatException e) {
	        return "redirect:/error"; 
	    }
	}

}
