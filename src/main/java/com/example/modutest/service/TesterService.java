package com.example.modutest.service;

import com.example.modutest.repository.TesterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "Tester Service")
@RequiredArgsConstructor
public class TesterService {
    private final TesterRepository testerRepository;
}
