package com.example.modutest.security.entity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
@Getter
public class PassUrl {

    private  String url;
    private  String method;




    private boolean passAuthentication(HttpServletRequest request , String passPath) {
        String path = request.getRequestURI();
        return path.startsWith(passPath);
    }//메소드도 구분 필요
}
