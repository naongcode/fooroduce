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
    //다른 출처의 자원을 공유할 수 있게 해두는 것.
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT")
                .allowedHeaders("*")
                .maxAge(3000); //원하는 시간만큼의 캐싱
    }

}
