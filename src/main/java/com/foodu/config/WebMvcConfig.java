package com.foodu.config;

import com.foodu.util.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
                .allowedMethods("GET", "POST", "PUT", "OPTIONS")
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "fingerprint") // 명시적으로 허용할 헤더 작성
                .allowCredentials(true) // 이 줄 꼭 필요
                .maxAge(3000);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthFilter)
                .addPathPatterns("/api/events/list", "/api/events/create");  // 필요한 경로만 지정
    }
}
