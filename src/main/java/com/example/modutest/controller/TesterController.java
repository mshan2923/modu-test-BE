package com.example.modutest.controller;

import com.example.modutest.dto.TestElement.TestMakeRequestDto;
import com.example.modutest.dto.TestElement.TestsResponseDto;
import com.example.modutest.dto.user.StatusResponseDto;
import com.example.modutest.entity.TestElement.Tester;
import com.example.modutest.security.detail.UserDetailsImpl;
import com.example.modutest.service.TesterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.awt.event.MouseEvent;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@RestControllerAdvice//전역 예외 처리
public class TesterController {// 임시 테스트 컨트롤러로 오인때문에 Tester로 했습니다!

    private final TesterService testerService;

    // 테스트 만들기 폼 페이지
    @GetMapping("/test/testMakeForm")
    public ModelAndView testMakeForm(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("testMakeForm");
        return modelAndView;
    }


    // 테스트 만들기
    @PostMapping("/test/testMakeForm")
    public ResponseEntity<StatusResponseDto> createTester(@RequestBody TestMakeRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails){//유저 인증 필요할때 추가
        try {
            TestsResponseDto tester = testerService.createTester(requestDto);
            return ResponseEntity.ok(new StatusResponseDto("테스트 작성 완료", 200));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new StatusResponseDto(e.getMessage(), 403));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusResponseDto("테스트 생성 중 오류 발생." + e.getMessage(), 500));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFoundEntity(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }//좀더 상세하게 나눠서 FE분들 이해하기 쉽게하기
}
