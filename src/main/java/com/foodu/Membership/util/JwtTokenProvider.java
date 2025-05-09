package com.foodu.Membership.util;

import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenProvider {
    public class SecretKey {
        public static String JWT_SECRT_KEY = "mysupersecureandlongenoughsecretkeyandwith123456"; //임의의 키
    }


    //토큰 생성
    public static String createToken(String user_id) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("HS256", "JWT") //Header값 ("alg", "typ")을 설정
                .claim("id", user_id) //쿠키에 저장되는 유저 데이터
                .setSubject(user_id) //유저의 인증 정보를 담고 있다는 토큰이라는 말
                .setIssuedAt(now) //토큰 생성 시간
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  //토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, SecretKey.JWT_SECRT_KEY)
                .compact(); //토큰 생성
    }


    //토큰 유효성 검사
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecretKey.JWT_SECRT_KEY)  // 서명 키로 서명 검증
                    .build()
                    .parseClaimsJws(token);  // 토큰을 파싱하면서 서명 검증

            return true;
        } catch (Exception e) {

            return false;
        }
    }


}