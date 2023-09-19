package com.example.modutest.service;

import com.example.modutest.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "Coment Service")
@RequiredArgsConstructor
public class ComentService {
    private final CommentRepository commentRepository;
}
