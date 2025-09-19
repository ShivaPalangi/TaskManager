package com.example.TaskManager.repository;

import com.example.TaskManager.entity.Company;
import com.example.TaskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("""
            SELECT DISTINCT c FROM Company c
            JOIN c.teams t
            JOIN t.memberships m
            WHERE m.employee = :user
            AND LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            """)
    List<Company> findCompaniesByUserAndNameContaining(
            @Param("user") User user,
            @Param("searchTerm") String searchTerm);

    List<Company> findAllByOwner(User owner);
    List<Company> findAllByOwnerAndNameContaining(User owner, String searchTerm);
    boolean existsByIdAndOwner(Long id, User owner);
}
