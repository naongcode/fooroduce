package com.foodu.config;

import com.foodu.util.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:5173",
                        "http://localhost:8080",
                        "https://fooroduce.7team.xyz",
                        "https://api.fooroduce.7team.xyz")
                .allowedMethods("GET", "POST", "PUT")
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With") // 명시적으로 허용할 헤더 작성
                .allowCredentials(true) // 이 줄 꼭 필요
                .maxAge(3000);
    }
}
