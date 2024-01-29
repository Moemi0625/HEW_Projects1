package com.example.webt.entity;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "webt")
@Data
public class Webt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    //タイトル
    @Column(name = "title")
    private String title;

    //作者名
    @Column(name = "author")
    private String author;

    //あらすじ
    @Column(name = "synopsis")
    private String synopsis;

    //ジャンル
    @Column(name = "genres")
    private List<String> genres;

    //レビュー（★）
    @Column(name = "rating")
    private Integer rating;

    //連載開始年（yyyy）
    @Column(name = "start_year")
    private Integer startYear;
    
    //画像パス
    @Column(name = "image_path")
    private String imagePath;
    
    //アップロードする画像ファイル
    @Transient
    private MultipartFile imageFile;
    
    //ステータス（完結:Y/N）
    @Column(name = "done")
    private String done;
}
