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

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "synopsis")
    private String synopsis;

    @Column(name = "genres")
    private List<String> genres;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "start_year")
    private Integer startYear;
    
    @Column(name = "image_path")
    private String imagePath;
    
    @Transient
    private MultipartFile imageFile;
    
    @Column(name = "done")
    private String done;
}
