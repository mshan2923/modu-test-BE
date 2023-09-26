package com.example.modu.security;

import com.example.modu.dto.user.StatusResponseDto;
import com.example.modu.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
@RequiredArgsConstructor
public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
//    private final JwtUtil jwtUtil;
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
//    {
//        UsernamePasswordAuthenticationToken authRequest
//                = UsernamePasswordAuthenticationToken.unauthenticated(obtainUsername(request), obtainPassword(request));
//
//        setDetails(request, authRequest);
//
//        try
//        {
//            return getAuthenticationManager().authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            obtainUsername(request),
//                            obtainPassword(request),
//                            null
//                    )
//            );
//        }catch (AuthenticationException e){
//            throw new RuntimeException("유효하지 않은 데이터 " + e.getMessage());
//        }
//
//    }

    // 토큰이 필요 없는 URL 패턴==============================================
    private static final String[] FILTER_IGNORE_URLS = {"/login","/signup"};


    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        String token = jwtUtil.resolveToken(request);
// GET 요청, login, signup 요청에 대해서는 필터를 거치지 않음. ==========================
        String requestURI = request.getRequestURI();
        String requstMethod = request.getMethod();

        if("GET".equalsIgnoreCase(requstMethod)){
            filterChain.doFilter(request,response);
            return;
        }
        if(isIgnoredURL(requestURI)){
            filterChain.doFilter(request,response);
            return;
        }
//===================================================================================


        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
//handleTokenError(response, "Token Null error", HttpStatus.UNAUTHORIZED);
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }
// Token Null Error 핸들링 =======================================================
        else{
            jwtExceptionHandler(response, "Token NULL error", HttpStatus.UNAUTHORIZED.value());
            return;
        }
//================================================================================
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username);//인증ㄱ개체
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new StatusResponseDto(msg, statusCode));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private boolean isIgnoredURL(String requestURI) {
        for (String ignoredUrl : FILTER_IGNORE_URLS) {
            if (requestURI.endsWith(ignoredUrl)) {
                return true;
            }
        }
        return false;
    }
}
