package com.example.modu.repository;

import com.example.modu.entity.TestElement.UserTestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTestResultRepository extends JpaRepository<UserTestResult, Long> {
    List<UserTestResult> findAllByUser_Id(Long id);
}
