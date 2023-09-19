package com.example.modutest.controller;

import com.example.modutest.service.JwtService;
import com.example.modutest.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j(topic = "User Controller")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
@ControllerAdvice//전역 예외 처리
public class UserController {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;

    @GetMapping("/loginForm")
    private String loginPage()
    {
        return "login";
    }// @RestController , @RestControllerAdvice 둘중 하나 있으면 문자열로 리턴

    /*@GetMapping("/signup")
    private String signupPage()
    {
        return "signup";
    }*/


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFoundEntity(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }//좀더 상세하게 나눠서 FE분들 이해하기 쉽게하기
}
