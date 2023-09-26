package com.example.modu.entity.TestElement;

import com.example.modu.dto.TestElement.TestMakeRequestDto;
import com.example.modu.entity.Comment;
import com.example.modu.entity.Timestamped;
import com.example.modu.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tester")
@NoArgsConstructor
public class Tester extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "views", nullable = false, columnDefinition = "bigint default 0")
    private int views=0;

    @Column(name = "likes", nullable = false, columnDefinition = "bigint default 0")
    private int likes=0;

    @Column(name = "participates", nullable = false, columnDefinition = "bigint default 0")
    private int participates=0;

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

    // Tag FK - 하나의 테스트에 여러 결과
    @OneToMany(mappedBy = "tester", cascade = CascadeType.ALL)
    private List<Result> results = new ArrayList<>();

    public Tester(TestMakeRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.image = requestDto.getImage();
        this.category = requestDto.getCategory();
        this.questions = new ArrayList<>();
    }
    public void setUser(User user)
    {
        this.user = user;
    }

    public void increaseLikes(){
        this.likes += 1;
    }

    public void increaseParticipates(){
        this.participates += 1;
    }

    public void increaseViews(){
        this.views += 1;
    }
}
