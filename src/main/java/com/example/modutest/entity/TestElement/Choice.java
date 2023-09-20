package com.example.modutest.entity.TestElement;

import com.example.modutest.dto.TestElement.ChoiceDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "choice")
@NoArgsConstructor
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "isCorrect",nullable = false)
    private boolean isCorrect;

//    @Column(name = "score",nullable = false)
//    private int score;

    //질문 FK -> 질문 하나에 여러 선택지
    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    public Choice(ChoiceDto dto){
        this.content = dto.getContent();
        this.isCorrect = dto.isCorrect();
    }
}
