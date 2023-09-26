package com.example.modu.dto.TestElement;

import com.example.modu.entity.TestElement.Choice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChoiceDto {
    private String content;
    private boolean correct;

    public ChoiceDto(Choice choice)
    {
        content = choice.getContent();
        correct = choice.isCorrect();
    }
}
