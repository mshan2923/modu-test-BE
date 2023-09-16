package com.example.modutest.entity.TestElement;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table()
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = true)
    private  String image;
    //테스트 FK
}
