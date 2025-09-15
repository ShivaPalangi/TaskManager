package com.example.TaskManager.repository;

import com.example.TaskManager.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByIdAndCompanyId(Long id, Long companyId);
    List<Team> findAllByCompanyId(Long companyId);
    List<Team> findAllByCompanyIdAndNameContainingIgnoreCase(Long companyId, String name);
    boolean existsByIdAndCompanyId(Long id, Long companyId);
}
