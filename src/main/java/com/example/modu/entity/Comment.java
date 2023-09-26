package com.example.modu.entity;

import com.example.modu.dto.comment.CommentRequestDto;
import com.example.modu.entity.TestElement.Tester;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
//@Setter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    //회원 정보 FK
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    //테스트 FK
    @ManyToOne
    @JoinColumn(name = "testerId")
    private Tester tester;

    public Comment(CommentRequestDto commentRequestDto){
        this.content = commentRequestDto.getContent();
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setTester(Tester tester){
        this.tester = tester;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
