package com.foodu.Membership.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Integer userId;
    private String token;
    private String role;
    private String message;

    public LoginResponse(String token, String role, String message) {
        this.userId = null;  // 또는 -1, 또는 의미 없는 값
        this.token = token;
        this.role = role;
        this.message = message;
    }
}
