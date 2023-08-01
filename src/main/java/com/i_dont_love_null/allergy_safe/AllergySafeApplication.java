package com.i_dont_love_null.allergy_safe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy
public class AllergySafeApplication {

	public static void main(String[] args) {

		SpringApplication.run(AllergySafeApplication.class, args);
	}

}
