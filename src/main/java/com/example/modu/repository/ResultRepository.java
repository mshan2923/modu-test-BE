package com.example.modu.repository;

import com.example.modu.entity.TestElement.Result;
import com.example.modu.entity.TestElement.Tester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByTesterAndScore(Tester tester, int score);

}
