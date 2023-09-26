package com.example.modu.repository;

import com.example.modu.entity.TestElement.Tester;
import com.example.modu.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TesterRepository extends JpaRepository<Tester, Long> {
    List<Tester> findAllByUser(User user);
    Page<Tester> findAllByCategory(String category, Pageable pageable);

    Page<Tester> findAllByOrderByParticipatesDesc(Pageable pageable);

}
