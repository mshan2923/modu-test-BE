package com.example.modu.security;

import com.example.modu.Handler.AuthFailureHandler;
import com.example.modu.Handler.AuthSuccessHandler;
import com.example.modu.service.UserDetailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@Slf4j(topic = "SecurityConfig")
@Configuration
//@EnableWebSecurity//spring Security 적용
@RequiredArgsConstructor
public class SecurityConfig {

    //private final JwtUtil jwtUtil;
    private final UserDetailService userDetailService;
    //private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure()//모든 기능 사용 안함
    {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
    }
    @Bean
    public SimpleCorsFilter simpleCorsFilter(){ return new SimpleCorsFilter();}

    /*
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(6000L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> filterBean = new FilterRegistrationBean<>(new CorsFilter());
        filterBean.addUrlPatterns("/**");
        filterBean.setOrder(0);

        return filterBean;
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        try {
            http.httpBasic(basic -> basic.disable());//basic Auth filter 비활성화
            http.csrf(t -> t.disable());
            //http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                //세션 대신 JWT 쓸때
            http.requiresChannel(channel -> channel.anyRequest().requiresInsecure());


            http.authorizeHttpRequests(auth ->
                    auth
                            .requestMatchers("/*").permitAll()
                            .requestMatchers("/api/**").permitAll()
                            .anyRequest().authenticated()
                    );
            http.cors(cors -> cors.configure(http));

            http.formLogin(formLogin ->
                    formLogin.loginPage("/api/user/loginForm")//<-- 서비스 로직 으로?
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .loginProcessingUrl("/api/user/login")
                            .successHandler(new AuthSuccessHandler())
                            .failureHandler(new AuthFailureHandler())

            );
        }catch (Exception e)
        {
            log.warn("Security Error : " + e.getMessage());
        }

        //http.addFilterBefore()

        return http.build();
    }

    @Bean//인증 관리자 관련 설정
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

}
