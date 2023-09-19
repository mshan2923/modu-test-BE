package com.example.modutest.entity;

import com.example.modutest.entity.TestElement.Tester;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comment")
@NoArgsConstructor
public class Comment {
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
}
