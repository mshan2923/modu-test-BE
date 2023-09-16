package com.example.modutest.entity.TestElement;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table()
@Getter
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private boolean isCorrect;
    @Column(nullable = false)
    private int score;
    //질문 FK
}
