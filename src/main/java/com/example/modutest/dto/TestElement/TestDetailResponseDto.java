package com.example.modutest.dto.TestElement;

import com.example.modutest.dto.comment.CommentResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class TestDetailResponseDto extends TestsResponseDto{
    private String content;
    private List<CommentResponseDto> comments;
}
