package com.example.modutest.entity.TestElement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tag")
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    // 하나의 테스트에 여러 태그
    @ManyToOne
    @JoinColumn(name = "testerId")
    private Tester tester;
}
