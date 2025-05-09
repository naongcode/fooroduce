package com.foodu.Membership.config;

import com.foodu.Membership.util.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;

public class WebMvcConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;


}