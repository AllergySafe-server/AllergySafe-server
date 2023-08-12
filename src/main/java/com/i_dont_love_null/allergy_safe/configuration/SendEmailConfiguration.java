package com.i_dont_love_null.allergy_safe.configuration;

import com.i_dont_love_null.allergy_safe.security.dto.PasswordResetResponse;
import com.i_dont_love_null.allergy_safe.security.dto.SendPasswordResetEmailResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendEmailConfiguration {
    @Bean
    public SendPasswordResetEmailResponse sendPasswordResetEmailResponse() {
        return new SendPasswordResetEmailResponse();
    }

    @Bean
    public PasswordResetResponse passwordResetResponse() {

        return new PasswordResetResponse();
    }
}
