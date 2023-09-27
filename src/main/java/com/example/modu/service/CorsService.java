package com.example.modu.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Slf4j(topic = "Custom CORS Service")
@Service
public class CorsService {

    // + 발급 되었을때 위치랑 요청했을때의 클라이언트 주소가 같은지 추가로 확인해 CSRF 방어

    private final String[] passUrls = {"http://localhost"};

    private boolean checkCors(HttpServletRequest request , String passPath) {
        String path = request.getRemoteUser();
        return path.startsWith(passPath);
    }// 확인 필요

    public void validateUrl(HttpServletRequest request)
    {
        log.info("---> Url : " + request.getRequestURI()  + " / " + request.getMethod() + " / Protocol : " + request.getProtocol() + " / Origin : " + request.getHeader("Origin")
                + "\n ContextPath : " + request.getContextPath() + " / User : " + request.getRemoteUser() + " | ServletPath : "  + request.getServletPath() + " | Host : " + request.getRemoteHost()
                + " / PORT  Local: " + request.getLocalPort() + " / Remote : " + request.getRemotePort() + " / Server : " + request.getServerPort());

        //request.getLocalPort() / request.getHeader("Origin")
        if (false)
        {
          throw new BadCredentialsException("유효하지 않은 접근 : Header.Origin : " + request.getHeader("Origin"));
        }

    }
}
