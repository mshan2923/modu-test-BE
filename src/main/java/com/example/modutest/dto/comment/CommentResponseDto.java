package com.example.modutest.dto.comment;

import com.example.modutest.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String username;
    private String content;

    public CommentResponseDto(Comment comment){
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
    }
}
