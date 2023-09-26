package com.example.modu.entity.TestElement;
import com.example.modu.dto.TestElement.QuestionDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
    private com.example.modu.entity.TestElement.Tester tester;

    // 하나의 질문에 여러 보기
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Choice> choices = new ArrayList<>();

    public Question(QuestionDto dto){
        this.title = dto.getTitle();
        this.image = dto.getImage();
        this.choices = new ArrayList<>();
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }
}
