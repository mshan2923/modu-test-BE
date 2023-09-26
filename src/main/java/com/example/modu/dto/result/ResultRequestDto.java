package com.example.modu.dto.result;

import com.example.modu.entity.TestElement.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResultRequestDto {
    private String image;
    private String content;
    private int score;

}
