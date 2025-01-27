package com.example.modutest.entity.TestElement;

import com.example.modutest.dto.TestElement.TestMakeRequestDto;
import com.example.modutest.entity.Comment;
import com.example.modutest.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tester")
@NoArgsConstructor
public class Tester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "views", nullable = false, columnDefinition = "bigint default 0")
    private Long views=0L;

    @Column(name = "likes", nullable = false, columnDefinition = "bigint default 0")
    private Long likes=0L;

    // User FK - 하나의 유저가 여러 테스트를 생성
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    // Test FK - 하나의 테스트가 여러 질문 생성
    @OneToMany(mappedBy = "tester", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    // Comment FK - 하나의 테스트에 여러 댓글
    @OneToMany(mappedBy = "tester", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // Tag FK - 하나의 테스트에 여러 태그
    @OneToMany(mappedBy = "tester", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    public Tester(TestMakeRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.image = requestDto.getImage();
        this.questions = new ArrayList<>();
    }

}
