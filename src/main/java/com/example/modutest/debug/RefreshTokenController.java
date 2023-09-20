package com.example.modutest.debug;

import com.example.modutest.security.detail.UserDetailsImpl;
import com.example.modutest.service.JwtService;
import com.example.modutest.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
@ControllerAdvice//전역 예외 처리
public class RefreshTokenController {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    //일단 쿠키를 2개를 주고 AccessToken을 30분 정도 짧은 수명으로 재 발급 받는 형태인데
    //쿠키값을 덮어쓰기가 ... 못하나 , 왜인지 모르겠는데 RefreshToken이 수명이 3일 인데 수명이 다되는 경우가 발생하네요...
    //
    @GetMapping("/SetTest")
    private ResponseEntity<String> AddCookie(HttpServletResponse response)
    {
        jwtService.CreateRefreshToken(response);
        return ResponseEntity.ok("----");
    }
    @GetMapping("/GetTest")
    private ResponseEntity<String> jwtTest(@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

            return ResponseEntity.ok("Success : " + userDetails.getUsername());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFoundEntity(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
