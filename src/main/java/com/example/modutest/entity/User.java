package com.example.modutest.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table()
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = true)
    private String profile;
}
