package com.example.modutest.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table()
@Getter
public class Tester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
