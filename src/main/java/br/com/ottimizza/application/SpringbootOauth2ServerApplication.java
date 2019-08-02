package br.com.ottimizza.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@SpringBootApplication
public class SpringbootOauth2ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootOauth2ServerApplication.class, args);
	}

	@Bean
	public SpringSecurityDialect securityDialect() {
		return new SpringSecurityDialect();
	}

}
