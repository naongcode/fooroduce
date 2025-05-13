//package com.foodu.config;
//
//import com.foodu.util.JwtAuthFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//    @Autowired
//    private JwtAuthFilter jwtAuthFilter;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry){
//        registry.addMapping("/**")
//                .allowedOriginPatterns(
//                        "http://localhost:5173",
//                        "http://localhost:8080",
//                        "https://fooroduce.7team.xyz",
//                        "https://api.fooroduce.7team.xyz")
//                .allowedMethods("GET", "POST", "PUT")
//                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With") // 명시적으로 허용할 헤더 작성
//                .allowCredentials(true) // 이 줄 꼭 필요
//                .maxAge(3000);
//    }
//}

package com.foodu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:5173",        // React 개발 서버 등
                        "http://localhost:8080",        // Postman or 직접 테스트 시
                        "https://fooroduce.7team.xyz",  // 실제 프론트 배포 주소
                        "https://api.fooroduce.7team.xyz")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")               // 헤더 전체 허용
                .allowCredentials(true)            // 쿠키 등 포함
                .maxAge(3600);
    }
}
