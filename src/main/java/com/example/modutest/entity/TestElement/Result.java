package com.example.modutest.entity.TestElement;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table()
@Getter
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //테스트 FK
    //결과점수????
    @Column(nullable = true)
    private String image;
    @Column(nullable = false)
    private String content;

}
