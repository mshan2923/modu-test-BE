package com.example.modutest.security;

import com.example.modutest.security.detail.UserDetailsServiceImpl;
import com.example.modutest.security.fillter.LoginAuthFilter;
import com.example.modutest.security.fillter.TokenAuthFilter;
import com.example.modutest.util.JwtUtil;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "SecurityConfig")
@Configuration
@EnableWebSecurity//spring Security 적용
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
    {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public TokenAuthFilter tokenAuthFilter()
    {
        return new TokenAuthFilter(userDetailsService, jwtUtil);
    }
    @Bean
    public LoginAuthFilter loginAuthFilter() throws Exception
    {
        LoginAuthFilter filter = new LoginAuthFilter(jwtUtil,"/api/user/login", "/" , "/api/user/loginForm");
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        try {
            http.csrf(t -> t.disable());//CSRF 설정
            http.sessionManagement((sesstion) ->
                    sesstion.sessionCreationPolicy(SessionCreationPolicy.STATELESS));//세션 비활성화

            http.authorizeHttpRequests(authHttpReq ->
                    authHttpReq
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()//static 리소스 접근
                            .requestMatchers("/").permitAll()
                            .requestMatchers("/api/**").permitAll()//필터 커스텀 이면 적용X
                            .anyRequest().authenticated()// 그외 인증 처리
            );


            http.formLogin(formLogin ->
                    formLogin.loginPage("/api/user/loginForm").permitAll()
                            .usernameParameter("username")
                            .passwordParameter("password")

            );


        }catch (Exception e)
        {
            log.warn("Redirect Error : " + e.getMessage());
            //throw new Exception("Security Error : " + e.getMessage());
        }

        http.addFilterBefore(tokenAuthFilter(), LoginAuthFilter.class);
        http.addFilterBefore(loginAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

