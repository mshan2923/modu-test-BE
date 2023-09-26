package com.example.modu.dto.TestElement;

import com.example.modu.entity.TestElement.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class QuestionDto {
    private String title;
    private String image;
    private List<ChoiceDto> choices;

    public QuestionDto(Question question)
    {
        title = question.getTitle();
        image = question.getImage();
        choices = question.getChoices().stream().map(ChoiceDto::new).toList();
    }

}
