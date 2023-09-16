package com.example.modutest.security;

import com.example.modutest.security.detail.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j(topic = "SecurityConfig")
@Configuration
@EnableWebSecurity//spring Security 적용
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
    {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        try {
            http.csrf(t -> t.disable());//CSRF 설정
            //http.sessionManagement((sesstion) ->
            //        sesstion.sessionCreationPolicy(SessionCreationPolicy.STATELESS));//세션 비활성화

            http.authorizeHttpRequests(authHttpReq ->
                    authHttpReq
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()//static 리소스 접근
                            .requestMatchers("/").permitAll()
                            .requestMatchers("/api/**").permitAll()//필터 커스텀 이면 적용X
                            .anyRequest().authenticated()// 그외 인증 처리
            );


            http.formLogin(formLogin ->
                    formLogin.loginPage("/api/user/login").permitAll()// /api/user/auth/login
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .loginProcessingUrl("/api/user/login")
                            //.successHandler()
                            //.failureHandler()
            );


        }catch (Exception e)
        {
            log.warn("Redirect Error : " + e.getMessage());
            throw new Exception("Security Error : " + e.getMessage());
        }

        return http.build();
    }
}

