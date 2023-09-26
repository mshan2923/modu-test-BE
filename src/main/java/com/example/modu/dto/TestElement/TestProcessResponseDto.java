package com.example.modu.dto.TestElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class TestProcessResponseDto {
    //질문들과 보기들 반환
    private List<QuestionDto> questions;
    private int questionCount;

    public TestProcessResponseDto(List<QuestionDto> questions)
    {
        this.questions = questions;
        this.questionCount = questions.size();
    }
}
