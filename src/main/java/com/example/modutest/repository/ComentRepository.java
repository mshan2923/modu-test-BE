package com.example.modutest.repository;

import com.example.modutest.entity.Coment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentRepository extends JpaRepository<Coment, Long> {
}
