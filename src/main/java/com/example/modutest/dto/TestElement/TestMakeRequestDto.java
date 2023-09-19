package com.example.modutest.dto.TestElement;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class TestMakeRequestDto {
    private Long userId;
    private String title;
    private String content;
    private String image;
    private List<QuestionDto> questions;
}
