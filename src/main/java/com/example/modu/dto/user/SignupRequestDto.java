package com.example.modu.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private String nickname;
    private MultipartFile image;
}
