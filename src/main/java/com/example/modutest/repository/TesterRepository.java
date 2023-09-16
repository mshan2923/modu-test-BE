package com.example.modutest.repository;

import com.example.modutest.entity.Tester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TesterRepository extends JpaRepository<Tester, Long> {
}
