package com.example.modu.entity;

import com.example.modu.dto.user.UserUpdateRequestDto;
import com.example.modu.entity.TestElement.Tester;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
//@Setter
@Table(name = "user")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,}$")
    private String username;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false)
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "image", nullable = true)
    private String image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tester> tests = new ArrayList<>();



    public User(String username, String email, String password, String image, String nickname)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
        this.nickname = nickname;
    }
    public List<Tester> addTest(Tester tester)
    {
        this.tests.add(tester);
        return this.tests;
    }

    @Override//권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override//계정 만료 여부
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override//계정 잠금 여부
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override//패스워드 만료 여부
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override//계정 사용 가능 여부
    public boolean isEnabled() {
        return true;
    }

    public void Update(String nickname, String password)
    {
        this.nickname = nickname;
        this.password = password;
    }
}
