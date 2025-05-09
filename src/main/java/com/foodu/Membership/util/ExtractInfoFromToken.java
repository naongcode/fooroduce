package com.foodu.Membership.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class ExtractInfoFromToken {


    //토큰에서 아이디 추출
    public static String extractUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JwtTokenProvider.SecretKey.JWT_SECRT_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}