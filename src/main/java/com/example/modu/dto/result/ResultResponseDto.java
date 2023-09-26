package com.example.modu.dto.result;

import com.example.modu.entity.TestElement.Result;
import com.example.modu.entity.TestElement.UserTestResult;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResultResponseDto {
    private int score;
    private int maxScore;

    private String nickname;

    public ResultResponseDto(int score, int maxScore, String nickname) {
        this.score = score;
        this.maxScore = maxScore;
        this.nickname = nickname;
    }
    public  ResultResponseDto(UserTestResult result)
    {
        score = result.getScore();
        maxScore = result.getMaxScore();
        nickname = result.getUser().getNickname();
    }
}
