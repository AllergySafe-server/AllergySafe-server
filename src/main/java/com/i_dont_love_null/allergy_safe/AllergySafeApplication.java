package com.i_dont_love_null.allergy_safe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy
public class AllergySafeApplication {

    public static void main(String[] args) {

        SpringApplication.run(AllergySafeApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("http://192.168.45.192:3000", "http://localhost:3000", "http://localhost:8080", "https://allergysafe.life")
                        .allowedHeaders("*")
                        .allowedMethods("*");
            }
        };
    }

}
