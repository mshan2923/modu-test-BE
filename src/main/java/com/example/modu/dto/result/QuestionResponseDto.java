package com.example.modu.dto.result;

import com.example.modu.dto.TestElement.ChoiceDto;
import com.example.modu.entity.TestElement.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class QuestionResponseDto {
    private Long questionId;
    private String title;
    private String image;
    private List<ChoiceResponseDto> choices;

    public QuestionResponseDto(Question question){
        this.questionId = question.getId();
        this.title = question.getTitle();
        this.image = question.getImage();
        this.choices = question.getChoices().stream().map(ChoiceResponseDto::new).collect(Collectors.toList());
    }
}
