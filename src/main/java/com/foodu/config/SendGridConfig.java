package com.foodu.config;


import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${spring.sendgrid.api-key}")
    private String apiKey;

    @Bean
    public SendGrid sendGrid(){
        if(apiKey == null){
            throw new IllegalArgumentException("SendGrid integration is not enabled");
        }
        return new SendGrid(apiKey);
    }

}
