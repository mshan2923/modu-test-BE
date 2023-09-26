package com.example.modu.dto.result;

import com.example.modu.entity.TestElement.Choice;
import lombok.Getter;

@Getter
public class ChoiceResponseDto {
    private Long choiceId;
    private String content;
    private boolean correct;

    public ChoiceResponseDto(Choice choice){
        this.choiceId = choice.getId();
        this.content = choice.getContent();
        this.correct = choice.isCorrect();
    }
}
