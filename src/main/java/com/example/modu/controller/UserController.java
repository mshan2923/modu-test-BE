package com.example.modu.controller;

import com.example.modu.dto.TestElement.TestsResponseDto;
import com.example.modu.dto.user.*;
import com.example.modu.entity.User;
import com.example.modu.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.jaas.SecurityContextLoginModule;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "User Controller")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/loginForm")
    private ResponseEntity<String> loginPage()
    {
        return ResponseEntity.badRequest().body("로그인 실패!");//---> 실패해야 뜸 , 성공시 하이재킹 해감
    }// @RestController , @RestControllerAdvice 둘중 하나 있으면 문자열로 리턴
    /*
    @GetMapping("/signupForm")
    private String signupPage()
    {
        return "signupForm";
    }*/
    @PostMapping("/signup")
    private ResponseEntity<StatusResponseDto> signup(@RequestBody SignupRequestDto signup) {
        return userService.signup(signup);
    }
    @GetMapping("/logout")
    private ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response)
    {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("Logout");
    }
    @PutMapping("/update")
    private ResponseEntity<StatusResponseDto> update(@AuthenticationPrincipal User user,
                                                     @RequestBody UserUpdateRequestDto update)
    {
        return userService.update(user, update);
    }
    @DeleteMapping("/delete")
    private ResponseEntity<StatusResponseDto> deleteUser(@AuthenticationPrincipal User user)
    {
        return userService.deleteUser(user);
    }

    /*
    @PostMapping("/login")
    private ResponseEntity<StatusResponseDto> login(@RequestBody LoginRequestDto login) {
        return userService.login(login);
    }*///Security 가 처리


    @GetMapping("/mypage") //단순 페이지 이동이 아닌 초기 로드시 정보
    private ResponseEntity<UserDataResponse> myPage(@AuthenticationPrincipal User user)
    {
        return userService.myPage(user);
    }

    @GetMapping("/tests")
    private ResponseEntity<List<TestsResponseDto>> makedTests(@AuthenticationPrincipal User user)
    {
        return userService.makedTests(user);
    }
    
    
    @GetMapping("/join")
    private ResponseEntity<List<TestsResponseDto>> joinTests(@AuthenticationPrincipal User user)
    {
        return userService.getJoinTests(user);
    }

}
