package com.example.modutest.dto.TestElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDto {
    private String title;
    private String image;
    private List<ChoiceDto> choices;
}
