package com.example.modu.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDataResponse {
    private Long userId;
    private String nickname;
}
