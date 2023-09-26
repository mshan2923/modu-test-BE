package com.example.modutest.service;

import com.example.modutest.entity.UserRoleEnum;
import com.example.modutest.util.JwtUtil;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtUtil jwtUtil;

    //쿠키 생성은 로그인 성공시 - LoginAuthFilter 에 있음

    // 먼저 AccessToken 확인후 , 유효하지 않으면 RefreshToken 확인해 재발급
    public boolean validateToken(String AccessToken, String RefreshToken, HttpServletResponse response) throws IOException {
        String pureToken = jwtUtil.substringToken(AccessToken);
        if (jwtUtil.validateToken(pureToken))
        {
            log.info("valid : Access");
            return true;
        }else
        {
            log.info("ReCreate Access");
            return validateRefreshToken(RefreshToken, response);
        }
    }

    public boolean validateRefreshToken(String RefreshToken, HttpServletResponse response) throws IOException {
        String pureToken = jwtUtil.substringToken(RefreshToken);
        if (! jwtUtil.valideteRefresh(pureToken, response))
        {
            log.warn("유효하지 않은 RefreshToken , 재 로그인 필요");
            response.sendRedirect("/api/user/loginForm");
            return false;
        }//재 로그인
        return true;
    }


    /// For Test
    public void CreateRefreshToken(HttpServletResponse response)
    {
        jwtUtil.addJwtToCookies("---", UserRoleEnum.USER, response);
    }
}
