package com.example.modutest.service;

import com.example.modutest.dto.user.SignupRequestDto;
import com.example.modutest.entity.User;
import com.example.modutest.repository.UserRepository;
import com.example.modutest.security.SecurityConfig;
import com.example.modutest.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@Slf4j(topic = "User Service")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean signup(SignupRequestDto requestDto)
    {
        Optional<User> checkUsername = userRepository.findByUsername(requestDto.getUsername());
        if (checkUsername.isPresent())
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");

        //---- 이메일 중복 검사 + unquie 옵션 추가

        String pattern = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";

        if (! Pattern.matches(pattern, requestDto.getPassword()))
        {
            throw new PatternSyntaxException("비밀번호 조건에 부합되지 않음", pattern, -1);
        }
        String cryptPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), requestDto.getEmail(), cryptPassword, requestDto.getImage());
        userRepository.save(user);

        return true;
    }
}
