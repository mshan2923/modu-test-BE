package com.example.modutest.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table()
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String title;

    //회원 정보 FK
    //테스트 FK
}
