package com.foodu.util;

import java.util.Date;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtTokenProvider {

    public static class SecretKey {
        public static final String JWT_SECRT_KEY = "mysupersecureandlongenoughsecretkeyandwith123456"; // 256비트 이상 권장
    }

    // 토큰 생성
    public static String createToken(String userId) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .claim("id", userId)
                .signWith(Keys.hmacShaKeyFor(SecretKey.JWT_SECRT_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 검사
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SecretKey.JWT_SECRT_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }
}
