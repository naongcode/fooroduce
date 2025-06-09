package com.foodu.Membership.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String role;
    private String message;
    private String userId;
    private String email;
}
