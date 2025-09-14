package com.example.TaskManager.service;

import com.example.TaskManager.entity.*;
import com.example.TaskManager.enums.MembershipRoles;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TaskMapper;
import com.example.TaskManager.repository.CompanyRepository;
import com.example.TaskManager.repository.MembershipRepository;
import com.example.TaskManager.repository.TaskRepository;
import com.example.TaskManager.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final CompanyRepository companyRepository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;
    private final MembershipRepository membershipRepository;


    public boolean canManageCompany(User user, Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new ResourceNotFoundException("Company with id %d not found".formatted(companyId)));
        return company.getOwner().getId().equals(user.getId());
    }


    public boolean canManageTeam(User user, Long teamId) throws AccessDeniedException {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Team with id %d not found".formatted(teamId)));
        Membership membership = membershipRepository.findByEmployeeAndTeam(user, team).orElseThrow(
                () -> new AccessDeniedException("You are not member of this team"));
        return membership.getRole() == MembershipRoles.OWNER || membership.getRole() == MembershipRoles.ADMIN;
    }


    public boolean canManageTask(User user, Long taskId) throws AccessDeniedException {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        Team team = task.getCreatedBy().getTeam();
        return canManageTeam(user, team.getId());
    }

    public boolean isMemberOfCompany(User user, Long companyId) {
        return membershipRepository.existsByEmployeeIdAndTeamCompanyId(user.getId(), companyId);
    }
}