package com.example.modutest.dto.TestElement;

import com.example.modutest.dto.comment.CommentResponseDto;
import com.example.modutest.entity.TestElement.Tester;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class TestsResponseDto {
    private String username;
    private String title;
    private String image;
    private Long views;
    private Long likes;
    private List<TagDto> tags;
    private List<CommentResponseDto> comments;

    public TestsResponseDto(Tester tester) {
        this.username = tester.getUser().getUsername();
        this.title = tester.getTitle();
        this.image = tester.getImage();
        this.views = tester.getViews();
        this.likes = tester.getLikes();
        this.tags = tester.getTags().stream().map(TagDto::new).collect(Collectors.toList());
        this.comments = tester.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

}
