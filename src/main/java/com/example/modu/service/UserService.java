package com.example.modu.service;

import com.example.modu.dto.TestElement.TestsResponseDto;
import com.example.modu.dto.user.*;
import com.example.modu.entity.TestElement.Tester;
import com.example.modu.entity.TestElement.UserTestResult;
import com.example.modu.entity.User;
import com.example.modu.repository.TesterRepository;
import com.example.modu.repository.UserRepository;
import com.example.modu.repository.UserTestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@Slf4j(topic = "User Service")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TesterRepository testerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTestResultRepository userTestResultRepository;

    public String cryptPassword(String password)
    {
        String pattern = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
        if (!Pattern.matches(pattern, password))
        {
            throw  new PatternSyntaxException("비밀 번호 조건에 부합 되지 않음", pattern, -1);
        }

        return passwordEncoder.encode(password);
    }

    public ResponseEntity<StatusResponseDto> signup(SignupRequestDto requestDto)
    {
        Optional<User> checkUsername = userRepository.findByUsername(requestDto.getUsername());
        if (checkUsername.isPresent())
            throw new IllegalArgumentException("중복된 사용자가 존재 합니다.");
        
        String cryptPassword = cryptPassword(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), requestDto.getEmail(), cryptPassword, "", requestDto.getNickname());
        userRepository.save(user);
        
        return ResponseEntity.ok(new StatusResponseDto("회원가입 성공" , 200));
    }

    /*
    public  ResponseEntity<StatusResponseDto> login(LoginRequestDto requestDto)
    {
        Optional<User> target = userRepository.findByUsername(requestDto.getUsername());
        if (target.isEmpty())
            throw new IllegalArgumentException("사용자가 존재 하지 않습니다.");

        if (! passwordEncoder.matches(requestDto.getPassword(), target.get().getPassword()))
        {
            throw new IllegalArgumentException("틀린 비밀번호");
        }

        return ResponseEntity.ok(new StatusResponseDto("로그인 성공", 200));
    }
    */

    public ResponseEntity<UserDataResponse> myPage(User user)
    {
        if (user == null)
            throw new IllegalArgumentException("인증 되지 않은 유저");
        return ResponseEntity.ok(new UserDataResponse(user.getId(), user.getNickname()));
    }

    public  ResponseEntity<List<TestsResponseDto>> makedTests(User user)
    {
        if (user == null)
            throw new IllegalArgumentException("인증 되지 않은 유저");

       return ResponseEntity.ok(testerRepository.findAllByUser(user).stream().map(TestsResponseDto::new).toList());
    }
    public ResponseEntity<StatusResponseDto> update(User user,
                                                    UserUpdateRequestDto updateValue)
    {
        if (user == null)
            throw new IllegalArgumentException("인증 되지 않은 유저");

        user.Update(updateValue.getNickname(), cryptPassword(updateValue.getPassword()));
        
        return ResponseEntity.ok(new StatusResponseDto("변경 완료", 200));
    }
    public ResponseEntity<StatusResponseDto> deleteUser(User user)
    {
        if (user == null)
            throw new IllegalArgumentException("인증 되지 않은 유저");

        userRepository.deleteById(user.getId());
        return ResponseEntity.ok(new StatusResponseDto("회원 탈퇴 완료", 200));
    }

    public ResponseEntity<List<TestsResponseDto>> getJoinTests(User user)
    {
        List<UserTestResult> testResults = userTestResultRepository.findAllByUser_Id(user.getId());
        TestsResponseDto[] Results = new TestsResponseDto[testResults.size()];//속도 때문에
        for(int i = 0; i < testResults.size(); i++ )
        {
            Results[i] = new TestsResponseDto(testResults.get(i).getTester());
        }
        return ResponseEntity.ok(Arrays.stream(Results).toList());
    }

}
