package com.example.modu.dto.user;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String password;
    private String nickname;
}
