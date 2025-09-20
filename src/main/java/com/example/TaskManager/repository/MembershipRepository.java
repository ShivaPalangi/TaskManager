package com.example.TaskManager.repository;

import com.example.TaskManager.entity.Membership;
import com.example.TaskManager.entity.Team;
import com.example.TaskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByEmployeeAndTeam(User employee, Team team);

    boolean existsByEmployeeIdAndTeamCompanyId(Long employeeId, Long companyId);

    boolean existsByEmployeeAndTeamId(User user, Long teamId);

    Optional<Membership> findByIdAndTeamId(Long id, Long teamId);

    Optional<Membership> findByEmployeeAndTeamId(User employee, Long team);

    Optional<Membership> findByEmployeeAndTeamIdAndTeamCompanyId(User employee, Long teamId, Long companyId);

    List<Membership> findAllByTeamId(Long teamId);

    @Query("""
            SELECT m FROM Membership m
            JOIN m.employee e
            WHERE m.team.id = :teamId
            AND (LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
            """)
    List<Membership> findByTeamIdAndEmployeeNameContaining(
            @Param("teamId") Long teamId,
            @Param("searchTerm") String searchTerm);

    Optional<Membership> findByEmployeeIdAndTeamId(Long employeeId, Long teamId);
    boolean existsByEmployeeIdAndTeamId(Long employeeId, Long teamId);
    List<Membership> findAllByEmployee(User employee);
}
