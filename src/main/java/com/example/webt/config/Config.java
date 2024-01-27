package com.example.webt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {
    
    public static final String IMAGE_PATH = "C:/Users/s.obuchi/Pictures/webt/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
            .addResourceLocations("file:///" + IMAGE_PATH);
    }
}
