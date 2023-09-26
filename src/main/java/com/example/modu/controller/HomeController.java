package com.example.modu.controller;

import com.example.modu.dto.TestElement.TestsResponseDto;
import com.example.modu.service.TesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeController {
    private final TesterService testerService;

    @GetMapping("/")//초기 정보 로드 , /api/tests 랑 같음
    private List<TestsResponseDto> Home(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size)
    {
        return testerService.getAllTests(page, size);
    }
}
