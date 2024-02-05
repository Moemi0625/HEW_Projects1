package com.example.webt.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.webt.entity.Webt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class WebtData {

    private Integer id;

    @NotBlank(message = "作品タイトルを入力してください")
    private String title;

    @NotBlank(message = "作者名を入力してください（作者が不明なら「不明」）")
    private String author;

    @NotEmpty(message = "ジャンルを少なくとも1つ選択してください")
    private List<String> genres;
    
    // レビュー
    private Integer rating;

    // Getter method for rating
    public Integer getRating() {
        return rating;
    }
    
    // Setter method for rating
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    // 連載開始年
    private Integer startYear;
    
    // startYearの形式をyyyyに固定
    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    /*** 完結したか（Y/N）*/
    private String done;

    // あらすじ
    private String synopsis;
    
    // ImagePath
    private MultipartFile imageFile;
    
    public Webt toEntity() {
        Webt webt = new Webt();
        webt.setId(id);
        webt.setTitle(title);
        webt.setAuthor(author);

        if (imageFile != null) {
            webt.setImagePath(imageFile.getOriginalFilename());
        }

        webt.setGenres(genres);
        webt.setStartYear(startYear);
        webt.setSynopsis(synopsis);
        webt.setDone(done);

        return webt;
    }
}