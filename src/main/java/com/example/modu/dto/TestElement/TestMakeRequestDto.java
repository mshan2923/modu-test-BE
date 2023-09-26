package com.example.modu.dto.TestElement;

import com.example.modu.dto.result.ResultRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@NoArgsConstructor
public class TestMakeRequestDto {
    private Long userId;
    private String title;
    private String content;
    private MultipartFile image;
    private String category;
    private int views;
    private int likes;
    private List<QuestionDto> questions;
    private List<ResultRequestDto> results;


}
