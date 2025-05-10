package com.foodu.util;


import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    // 비밀번호를 해시(bcrypt 방식)로 변환
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    // 비밀번호 비교: 입력값 vs 해시값
    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified;
    }

}
