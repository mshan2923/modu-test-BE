package com.example.modu.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "Custom CORS Service")
@Service
public class CorsService {

    // + 발급 되었을때 위치랑 요청했을때의 클라이언트 주소가 같은지 추가로 확인해 CSRF 방어
    public boolean validateUrl(HttpServletRequest request)
    {
        log.info("--- " + request.getRequestURI()  + " / " + request.getMethod() + " / " + request.getProtocol() + " / " + request.getHeader("Origin")
                + "\n" + request.getContextPath() + " / " + request.getRemoteUser() + " | "  + request.getServletPath() + " | " + request.getRemoteHost());

        return true;
    }
}
