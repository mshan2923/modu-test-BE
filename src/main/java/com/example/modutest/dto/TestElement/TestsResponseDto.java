package com.example.modutest.dto.TestElement;

import lombok.Getter;

import java.util.List;

@Getter
public class TestsResponseDto {
    private String username;
    private String title;
    private String image;
    private Long views;
    private Long likes;
    private List<TagDto> tags;
}
