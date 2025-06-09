package com.foodu.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        //CORS시 브라우저에서 API를 받는 서버가 요청을 받을 수 있는지 option메소드를 통해 먼저 서버를 호출
        //그 과정에서 request header에 실어보낸 JWT를 받지 못하여 CORS policy 오류가 뜰 수 있음.
        //그럴 땐 명시적으로 메소드가 OPTIONS일 경우 JWT를 체크하지 않게 함.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }



        String path = request.getRequestURI();
        if (
                path.equals("/api/users/signup") ||
                        path.equals("/api/users/login") ||
                        path.equals("/api/users/id-check")
        ) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Authorization header or invalid format.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("인증 토큰이 필요합니다.");
            return false;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(JwtTokenProvider.SecretKey.JWT_SECRT_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            request.setAttribute("userId", claims.get("id"));
            request.setAttribute("role", claims.get("role"));
            return true;
        } catch (JwtException e) {
            System.out.println("Error parsing token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("유효하지 않은 토큰입니다.");
            return false;
        }
    }
}
