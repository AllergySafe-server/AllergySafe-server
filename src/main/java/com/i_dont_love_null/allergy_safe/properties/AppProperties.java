package com.i_dont_love_null.allergy_safe.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application")
public class AppProperties {

    private String appName;

    private String appDomain;

    private String senderEmail;
}
