package com.example.simplespringsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class SimpleSpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleSpringSecurityApplication.class, args);
    }

}
