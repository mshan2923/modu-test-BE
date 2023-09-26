package com.example.modutest.dto.TestElement;

import com.example.modutest.dto.comment.CommentResponseDto;
import com.example.modutest.entity.TestElement.Tester;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TestDetailResponseDto extends TestsResponseDto{
    private String content;
    private List<CommentResponseDto> comments;

    public TestDetailResponseDto(Tester tester) {
        super(tester);
        this.content = tester.getContent();
        this.comments = tester.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }
}
