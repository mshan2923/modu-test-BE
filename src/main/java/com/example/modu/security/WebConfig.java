package com.example.modu.security;

import com.example.modu.Handler.AuthFailureHandler;
import com.example.modu.Handler.AuthSuccessHandler;
import com.example.modu.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebConfig implements WebMvcConfigurer{

    private final JwtUtil jwtUtil;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry)
//    {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
//                .allowedHeaders("*")
//                .allowCredentials(true)//프론트에서도 axios > allowCredentials 허용해야함
//                .maxAge(3600);
//        //로그인후 request응답 헤더에 Origin, Access-Control-Request-Method,Access-Control-Request-Headers이 필요
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("POST","GET","DELETE","PUT","PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource()
//    {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.addAllowedOriginPattern("*");
//
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedMethod("GET");
//        configuration.addAllowedMethod("POST");// * 이 있지만 혹시나 해서 넣어봄
//
//        configuration.addAllowedHeader("*");
//        configuration.setAllowCredentials(true);
//
//
//        configuration.addExposedHeader("Authorization");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }//프론트엔드에서 CORS 가 유효하면 , 여기로 옴


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//필터를 통과한 경우에만 한글 메세지가 나옴, 이 설정을 하면 필터를 지나기전에 반환되는 한글깨짐을 고칠수있음
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, CsrfFilter.class);


        http.cors(cors -> cors.configure(http));
        http.csrf(t -> t.disable());

        //세션사용안함
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//토큰없이 요청가능한 url /users/**, /boards/** 그외에는 권환학인 필수
        http
                .authorizeRequests()
                .requestMatchers("/**").permitAll()
                .requestMatchers("/api/**").permitAll()

                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new LoginAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

                    http.formLogin(formLogin ->
                    formLogin//.loginPage("/api/user/loginForm")//<-- 서비스 로직 으로? / login 으로? 아니면 없애거나?
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .loginProcessingUrl("/api/user/login")
                            .successHandler(new AuthSuccessHandler(jwtUtil))
                            .failureHandler(new AuthFailureHandler())
                    );


        return http.build();

    }
}
