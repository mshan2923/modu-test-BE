package com.example.modutest.entity.TestElement;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table()
@Getter
public class Tester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = true)
    private String image;
    @Column(nullable = false)
    private Long views;
    @Column(nullable = false)
    private Long like;

    //생성한 유저 정보 FK
}
