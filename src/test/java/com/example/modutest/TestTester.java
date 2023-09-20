package com.example.modutest;

import com.example.modutest.dto.TestElement.TestsResponseDto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class TestTester {

    @Test
    public void GenerateTest() throws JSONException {
        //Tester > Question > Choice
        JSONObject result = new JSONObject();
        JSONArray questions = new JSONArray();
        JSONObject ques = new JSONObject();
        JSONArray choices = new JSONArray();

        for (int q = 0; q < 3; q++)
        {
            JSONObject choice = new JSONObject();
            for (int c = 0; c < 3; c++)
            {
                choice.put("content" , "Choice content");
                choice.put("isCorrect", false);
                choices.put(c,  choice);
            }//보기 부분

            ques.put("title", "Question title");
            ques.put("choices", choices);//질문 부분 만들기

            questions.put(q, ques);//질문 부분 넣기
        }

        result.put("title", "Test title");
        result.put("content", "Test Content");
        result.put("questions", ques);

        System.out.println("---> " + result);
    }
}
