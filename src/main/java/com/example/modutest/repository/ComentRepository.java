package com.example.modutest.repository;

import com.example.modutest.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentRepository extends JpaRepository<Comment, Long> {
}