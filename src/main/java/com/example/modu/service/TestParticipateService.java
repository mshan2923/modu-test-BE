package com.example.modu.service;

import com.example.modu.dto.TestElement.ChoiceDto;
import com.example.modu.dto.TestElement.QuestionDto;
import com.example.modu.dto.TestElement.TestProcessResponseDto;
import com.example.modu.dto.result.ParticipateRequestDto;
import com.example.modu.dto.result.ResultResponseDto;
import com.example.modu.dto.result.TestStartResponseDto;
import com.example.modu.entity.TestElement.Choice;
import com.example.modu.entity.TestElement.Result;
import com.example.modu.entity.TestElement.Tester;
import com.example.modu.entity.TestElement.UserTestResult;
import com.example.modu.entity.User;
import com.example.modu.repository.*;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestParticipateService {

    private final TesterRepository testerRepository;
    private final ChoiceRepository choiceRepository;
    private final UserRepository userRepository;
    private final UserTestResultRepository userTestResultRepository;

    // 테스트 완료
    public ResultResponseDto participateTest(Long testId, ParticipateRequestDto dto) {
        User currentUser = getCurrentUser();
        Tester tester = findTesterById(testId);

        int userScore = 0;
        int maxScore = 0;

        for(Long choiceId : dto.getUserChoices()){
            Choice choice = choiceRepository.findById(choiceId).orElseThrow(()->new IllegalArgumentException("해당 번호의 보기가 없습니다."));
            if(choice.isCorrect()){
                userScore ++;
            }
            maxScore++;
        }

        /*try{
            userScore = (int) dto.getUserChoices().stream().filter(t -> choiceRepository.findById(t).get().isCorrect()).count();
        }catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("해당 번호의 보기가 없습니다.");
        }*/

        UserTestResult userTestResult = new UserTestResult(currentUser, userScore, maxScore, tester);

        tester.increaseParticipates();

        userTestResultRepository.save(userTestResult);

        return new ResultResponseDto(userTestResult);
    }

//    // 테스트 참여
//    public TestStartResponseDto testStart(Long testId) {
//        Tester tester = findTesterById(testId);
//        return new TestStartResponseDto(tester);
//    }

    // 현재 로그인한 회원 정보 가져오기
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) { //---------------
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUsername = userDetails.getUsername();
            return userRepository.findByUsername(currentUsername).orElseThrow(
                    () -> new IllegalArgumentException("인증된 사용자를 찾을 수 없습니다.")
            );
        } else {
            throw new IllegalStateException("올바른 인증 정보가 아닙니다.");
        }
    }

    // testId로 해당 test 가져오기
    private Tester findTesterById(Long testId) {
        return testerRepository.findById(testId).orElseThrow(
                () -> new IllegalArgumentException("해당 테스트를 찾을 수 없습니다.")
        );
    }


    public ResponseEntity<TestProcessResponseDto> getQuestions(Long testId)
    {
        Tester target = testerRepository.findById(testId).orElseThrow(
                () -> new IllegalArgumentException("해당 테스트를 찾을 수 없습니다."));

        return ResponseEntity.ok(new TestProcessResponseDto(target.getQuestions().stream().map(QuestionDto::new).toList()));
    }
}
