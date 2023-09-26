package com.example.modu.entity.TestElement;

import com.example.modu.dto.TestElement.ChoiceDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "choice")
@NoArgsConstructor
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "correct",nullable = false, columnDefinition = "boolean default false")
    private boolean correct;

//    @Column(name = "score",nullable = false)
//    private int score;

    //질문 FK -> 질문 하나에 여러 선택지
    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    public Choice(ChoiceDto dto){
        this.content = dto.getContent();
        this.correct = dto.isCorrect();
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
