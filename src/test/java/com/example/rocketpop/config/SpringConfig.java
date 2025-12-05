package com.example.rocketpop.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.rocketpop.repository.UserDatabase;

@Configuration
@EnableAutoConfiguration
public class SpringConfig {

    @Bean
    public UserDatabase userDatabase() {
        return new UserDatabase();
    }
}
