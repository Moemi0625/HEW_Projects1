package com.example.webt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.webt.entity")
public class Hew1Application {

    public static void main(String[] args) {
        SpringApplication.run(Hew1Application.class, args);
    }
}
