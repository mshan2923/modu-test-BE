package com.example.modutest.security.fillter;

import com.example.modutest.repository.UserRepository;
import com.example.modutest.security.detail.UserDetailsServiceImpl;
import com.example.modutest.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        String cookieValue = null;
        for (Cookie cookie : request.getCookies())
        {
            if (JwtUtil.AUTHO_Access_HEADER.equals(cookie.getName()))
            {
                cookieValue = cookie.getValue();
                break;
            }
        }
        String tokenValue = jwtUtil.substringToken(URLDecoder.decode(cookieValue, "UTF-8"));

        if (StringUtils.hasText(tokenValue))
        {
            if (!jwtUtil.validateToken(tokenValue))
                throw new ServletException("유효 하지 않은 토큰");

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

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
