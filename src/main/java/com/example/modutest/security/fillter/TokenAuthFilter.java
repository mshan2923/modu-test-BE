package com.example.modutest.security.fillter;

import com.example.modutest.security.detail.UserDetailsServiceImpl;
import com.example.modutest.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;

@Slf4j
public class TokenAuthFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    

    public TokenAuthFilter(UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }//====== Map 으로 예외 링크 만들기

    private boolean passAuthentication(HttpServletRequest request , String passPath) {
        String path = request.getRequestURI();
        return path.startsWith(passPath);
    }//메소드도 구분 필요

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        log.info("shouldNotFilter");

        if (PathRequest.toStaticResources().atCommonLocations().matches(request))
            return true;
        if (passAuthentication(request , "/api/user/login"))
            return true;
        if (passAuthentication(request, "/api/test") && request.getMethod().equals("GET"))
            return true;

        return false;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        log.info("doFilterInternal");

        String accessCookieValue = null;
        String refreshCookieValue = null;

        for (Cookie cookie : request.getCookies())
        {
            if (JwtUtil.AUTHO_Access_HEADER.equals(cookie.getName()))
            {
                accessCookieValue = cookie.getValue();
            }
            if (JwtUtil.AUTHO_Refresh_HEADER.equals(cookie.getName()))
            {
                refreshCookieValue = cookie.getValue();
            }
        }

        String accessTokenValue = jwtUtil.substringToken(URLDecoder.decode(accessCookieValue, "UTF-8"));
        String refreshTokenValue = jwtUtil.substringToken(URLDecoder.decode(refreshCookieValue, "UTF-8"));

        if (StringUtils.hasText(accessTokenValue) && StringUtils.hasText(refreshTokenValue))
        {
            if (!jwtUtil.validateToken(accessTokenValue))
            {
                if (!jwtUtil.valideteRefresh(refreshTokenValue, response))//유효 시간도 검사 + AccessToken 재발급도 함
                {
                    throw new ServletException("유효 하지 않은 토큰");
                }else
                {
                    //log.info("--- Recreate Access token");//재생성
                    Claims info = jwtUtil.getUserInfoFromToken(refreshTokenValue);
                    setAuthentication(info.getSubject());
                    filterChain.doFilter(request,response);
                    return;
                }//임시 - 재발급시
            }

            // 재발급시 값 다시 가져오든가 해야하는데 , Response에 다시 찾기?

            Claims info = jwtUtil.getUserInfoFromToken(accessTokenValue);

            if (info.getExpiration().after(new Date())){

                if (!jwtUtil.valideteRefresh(refreshTokenValue, response))
                {
                    response.sendError(400, "유효 기간 넘은 토큰");
                    throw new ServletException("유효 기간 넘은 토큰");
                }
            }

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e)
            {
                log.error(e.getMessage());
            }

            log.info("토큰 검증 완료 : " + response.getStatus());
        }else
        {
            log.warn("Token is Empty");
            throw new NullPointerException("Token is Empty");
        }

        filterChain.doFilter(request,response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        log.info("setAuthentication");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }//신임장 -> null
}
