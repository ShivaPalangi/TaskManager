package com.example.TaskManager.service;

import com.example.TaskManager.entity.*;
import com.example.TaskManager.enums.MembershipRoles;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.repository.*;
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
    private final TaskCategoryRepository taskCategoryRepository;


    public boolean canManageCompany(User user, Long companyId) {
        return companyRepository.existsByIdAndOwner(companyId, user);
    }


    public boolean canManageTeam(User user, Long teamId) throws AccessDeniedException {
        Membership membership = membershipRepository.findByEmployeeAndTeamId(user, teamId).orElseThrow(
                () -> new AccessDeniedException("You are not member of this team"));
        return membership.getRole() == MembershipRoles.OWNER || membership.getRole() == MembershipRoles.ADMIN;
    }

    public boolean canManageTeam(User user, Long teamId, Long companyId) throws AccessDeniedException {
        Membership membership = membershipRepository.findByEmployeeAndTeamIdAndTeamCompanyId(user, teamId, companyId).orElseThrow(
                () -> new AccessDeniedException("You are not member of this team"));
        return membership.getRole() == MembershipRoles.OWNER || membership.getRole() == MembershipRoles.ADMIN;
    }


    public boolean canManageTask(User user, Long taskId) {
        return taskRepository.existsByIdAndCreatedByEmployee(taskId, user);
    }


    public boolean isResponsible(Long taskId, User user) {
        return taskRepository.existsByIdAndResponsibleEmployee(taskId, user);
    }


    public boolean isMemberOfCompany(User user, Long companyId) {
        return membershipRepository.existsByEmployeeIdAndTeamCompanyId(user.getId(), companyId);
    }


    public boolean isMemberOfTeam(User user, Long teamId, Long companyId) {
        return membershipRepository.existsByEmployeeAndTeamId(user, teamId) &&
                teamRepository.existsByIdAndCompanyId(teamId, companyId);
    }


    public boolean canGetTaskCategoryDetail(User user, Long categoryId) {
        if ( taskCategoryRepository.existsByIdAndIsPrimaryTrue(categoryId) )
            return true;
        return taskCategoryRepository.existsByIdAndCreatedBy(categoryId, user);
    }


    public boolean canManagePrimaryTaskDetail(Long categoryId) {
        return taskCategoryRepository.existsByIdAndIsPrimaryTrue(categoryId);
    }



    public boolean canManageTaskCategory(Long categoryId, User user) {
        return taskCategoryRepository.existsByIdAndCreatedBy(categoryId, user);
    }

    public boolean canGetTaskDetail(User user, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        return membershipRepository.existsByEmployeeAndTeamId(user, task.getResponsible().getTeam().getId());
    }
}