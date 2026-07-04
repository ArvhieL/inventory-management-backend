package com.inventory.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (Crucial for allowing Postman to send POST requests)
                .csrf(csrf -> csrf.disable())

                // 2. Tell the Bouncer that EVERY request still needs a password
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())

                // 3. Keep using Basic Authentication (Username + Password)
                .httpBasic(withDefaults());

        return http.build();
    }
}