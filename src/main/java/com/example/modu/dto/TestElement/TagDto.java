package com.example.modu.dto.TestElement;

import com.example.modu.entity.TestElement.Tag;

public class TagDto {
    private String content;

    public TagDto(Tag tag){
        this.content = tag.getContent();
    }
}
