package com.example.modu.repository;

import com.example.modu.entity.TestElement.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
}
