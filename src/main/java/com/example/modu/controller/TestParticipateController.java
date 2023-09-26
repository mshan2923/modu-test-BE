package com.example.modu.controller;

import com.example.modu.dto.TestElement.ChoiceDto;
import com.example.modu.dto.result.ParticipateRequestDto;
import com.example.modu.dto.result.ResultResponseDto;

import com.example.modu.dto.result.TestStartResponseDto;
import com.example.modu.entity.User;
import com.example.modu.service.TestParticipateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Slf4j(topic = "Test Participate Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestParticipateController {

    private final TestParticipateService testParticipateService;

    //테스트 시작(진행)// 질문들과 보기 , 질문 갯수 반환
    @GetMapping("/participate/{testId}")
    public ResponseEntity<?> getQuestions(@PathVariable Long testId) {
        return testParticipateService.getQuestions(testId);
    }

    // 테스트 완료
    @PostMapping("/participate/{testId}")
    public ResultResponseDto participate(@PathVariable Long testId,
                                         @RequestBody ParticipateRequestDto dto) {
        return testParticipateService.participateTest(testId, dto);
    }

//    // 테스트 참여
//    @GetMapping("/participate/{testId}")
//    public TestStartResponseDto testStart(@PathVariable Long testId){
//        return testParticipateService.testStart(testId);
//    }
}
