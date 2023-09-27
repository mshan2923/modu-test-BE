package com.example.modu.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j(topic = "SecurityConfig")
@Configuration
@EnableWebSecurity//spring Security 적용
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.cors(t -> t.disable());//CorsService 에서 처리
        http.csrf(t -> t.disable());
        http.authorizeHttpRequests(req ->
                req.requestMatchers("/**").permitAll());

        return http.build();
    }
}
