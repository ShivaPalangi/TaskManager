package com.example.TaskManager.repository;

import com.example.TaskManager.entity.Membership;
import com.example.TaskManager.entity.Team;
import com.example.TaskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByEmployeeAndTeam(User employee, Team team);

    boolean existsByEmployeeIdAndTeamCompanyId(Long employeeId, Long companyId);
}
