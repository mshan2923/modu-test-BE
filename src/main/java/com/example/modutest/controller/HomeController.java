package com.example.modutest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@RestControllerAdvice//전역 예외 처리
public class HomeController {

    @GetMapping("/")
    private String Home()
    {
        return "index";
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFoundEntity(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }//좀더 상세하게 나눠서 FE분들 이해하기 쉽게하기
}
