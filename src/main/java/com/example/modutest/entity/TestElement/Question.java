package com.example.modutest.entity.TestElement;

import com.example.modutest.dto.TestElement.QuestionDto;
import com.example.modutest.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "question")
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "image", nullable = true)
    private  String image;

    // 하나의 테스트에 여러 질문
    @ManyToOne
    @JoinColumn(name = "testerId")
    private Tester tester;

    // 하나의 질문에 여러 보기
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Choice> choices = new ArrayList<>();

    public Question(QuestionDto dto){
        this.title = dto.getTitle();
        this.image = dto.getImage();
        this.choices = new ArrayList<>();
    }
}
