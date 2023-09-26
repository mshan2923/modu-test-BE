package com.example.modu.security;

import com.example.modu.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
@RequiredArgsConstructor
public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        UsernamePasswordAuthenticationToken authRequest
                = UsernamePasswordAuthenticationToken.unauthenticated(obtainUsername(request), obtainPassword(request));

        setDetails(request, authRequest);

        try
        {
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            obtainUsername(request),
                            obtainPassword(request),
                            null
                    )
            );
        }catch (AuthenticationException e){
            throw new RuntimeException("유효하지 않은 데이터 " + e.getMessage());
        }

    }
}
