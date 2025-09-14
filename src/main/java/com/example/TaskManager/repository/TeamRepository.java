package com.example.TaskManager.repository;

import com.example.TaskManager.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByIdAndCompanyId(Long id, Long companyId);
}
