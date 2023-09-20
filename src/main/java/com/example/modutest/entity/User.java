package com.example.modutest.entity;

import com.example.modutest.entity.TestElement.Tester;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
//@Setter
@Table(name = "user")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,}$")
    private String username;

    @Column(name = "email", nullable = false)
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "image", nullable = true)
    private String image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tester> tests = new ArrayList<>();

    public User(String username, String email, String password, String image)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
    }
}
