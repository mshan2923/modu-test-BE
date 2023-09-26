package com.example.modu.dto.result;

import com.example.modu.dto.TestElement.ChoiceDto;
import com.example.modu.dto.TestElement.QuestionDto;
import com.example.modu.entity.TestElement.Tester;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class TestStartResponseDto {
    private Long testerId;
    private String title;
    private String content;
    private String image;
    private String category;
    private List<QuestionResponseDto> questions;

    public TestStartResponseDto(Tester tester){
        this.testerId = tester.getId();
        this.title = tester.getTitle();
        this.content = tester.getContent();
        this.image = tester.getImage();
        this.category = tester.getCategory();
        this.questions = tester.getQuestions().stream().map(QuestionResponseDto::new).collect(Collectors.toList());
    }
}
