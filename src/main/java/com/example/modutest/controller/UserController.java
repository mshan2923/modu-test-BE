package com.example.modutest.controller;

import com.example.modutest.dto.user.SignupRequestDto;
import com.example.modutest.dto.user.StatusResponseDto;
import com.example.modutest.service.JwtService;
import com.example.modutest.service.UserService;
import com.example.modutest.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "User Controller")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
@ControllerAdvice//전역 예외 처리
public class UserController {

    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final UserService userService;

    @GetMapping("/loginForm")
    private String loginPage()
    {
        return "login";
    }// @RestController , @RestControllerAdvice 둘중 하나 있으면 문자열로 리턴

    @GetMapping("/signupFrom")
    private String signupPage()
    {
        return "signupFrom";
    }
    @PostMapping("/signup")
    private  ResponseEntity<StatusResponseDto> signup(@Valid @RequestBody SignupRequestDto signup,
                                                      BindingResult bindingResult)
    {
        FieldError fieldError = bindingResult.getFieldError();
        if (fieldError != null)
            return ResponseEntity.badRequest().body(new StatusResponseDto(fieldError.getDefaultMessage(), 400));


        if (userService.signup(signup))
            return  ResponseEntity.ok(new StatusResponseDto("회원 가입 성공", 200));
        else
            return ResponseEntity.badRequest().body(new StatusResponseDto("회원 가입 실패", 400));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFoundEntity(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }//좀더 상세하게 나눠서 FE분들 이해하기 쉽게하기
}
