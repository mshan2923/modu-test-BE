package com.example.modutest.service;

import com.example.modutest.dto.TestElement.ChoiceDto;
import com.example.modutest.dto.TestElement.QuestionDto;
import com.example.modutest.dto.TestElement.TestMakeRequestDto;
import com.example.modutest.dto.TestElement.TestsResponseDto;
import com.example.modutest.entity.TestElement.Choice;
import com.example.modutest.entity.TestElement.Question;
import com.example.modutest.entity.TestElement.Tester;
import com.example.modutest.entity.User;
import com.example.modutest.repository.TesterRepository;
import com.example.modutest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "Tester Service")
@RequiredArgsConstructor
public class TesterService {
    private final TesterRepository testerRepository;
    private final UserRepository userRepository;

    public TestsResponseDto createTester(TestMakeRequestDto requestDto){
        User currentUser = getCurrentUser();
        if(currentUser==null){
            throw new IllegalStateException("로그인한 사용자만 테스트를 작성할 수 있습니다.");
        }

        Tester tester = new Tester(requestDto);
        tester.setUser(currentUser);
        currentUser.getTests().add(tester);

        for(QuestionDto questionDto : requestDto.getQuestions()){
            Question question = new Question(questionDto);
            question.setTester(tester);
            tester.getQuestions().add(question);

            for(ChoiceDto choiceDto : questionDto.getChoices()){
                Choice choice = new Choice(choiceDto);
                choice.setQuestion(question);
                question.getChoices().add(choice);
            }
        }

        Tester saveTester = testerRepository.save(tester);
        TestsResponseDto testsResponseDto = new TestsResponseDto(saveTester);
        return testsResponseDto;
    }


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
}
