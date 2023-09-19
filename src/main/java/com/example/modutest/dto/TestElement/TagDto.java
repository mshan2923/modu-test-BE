package com.example.modutest.dto.TestElement;

import com.example.modutest.entity.TestElement.Tag;

public class TagDto {
    private String content;

    public TagDto(Tag tag){
        this.content = tag.getContent();
    }
}
