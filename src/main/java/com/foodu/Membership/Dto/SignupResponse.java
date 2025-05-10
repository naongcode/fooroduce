package com.foodu.Membership.Dto;



import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {

    private String user_id;
    private String email;
    private String role;



}
