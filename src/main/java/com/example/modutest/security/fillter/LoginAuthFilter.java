package com.example.modutest.security.fillter;

import com.example.modutest.entity.UserRoleEnum;
import com.example.modutest.security.detail.UserDetailsImpl;
import com.example.modutest.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "LoginAuthFilter")
public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter {
    private String afterSuccessUrl;
    private String afterFailUrl;
    private final JwtUtil jwtUtil;
    public LoginAuthFilter(JwtUtil jwtUtil, String processUrl, String afterSuccessUrl, String afterFailUrl)
    {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl(processUrl);
        this.afterSuccessUrl = afterSuccessUrl;
        this.afterFailUrl = afterFailUrl;
    }

    /*
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {

        UsernamePasswordAuthenticationToken authRequest
                = UsernamePasswordAuthenticationToken.unauthenticated(obtainUsername(request), obtainPassword(request));

        setDetails(request, authRequest);//서브클래스가 인증 요청의 세부 정보 속성에 입력되는 내용을 구성할 수 있도록 제공됩니다.

        if (!request.getMethod().equals("POST"))
            throw  new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());

        try
        {

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            obtainUsername(request),
                            obtainPassword(request),
                            null
                    )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("유효 하지 않는 데이터 : " + e.getMessage());
        }
    }
    */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException
    {
        log.info("로그인 성공");

        /*
                String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        jwtUtil.addJwtToCookie(token, response);
        */
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        jwtUtil.addJwtToCookies(username, UserRoleEnum.USER, response);

        if (!afterSuccessUrl.isEmpty())
            response.sendRedirect(afterSuccessUrl);
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException
    {
        log.info("로그인 실패 : " + failed.getMessage());

        if (!afterFailUrl.isEmpty())
            response.sendRedirect(afterFailUrl);
    }
}
