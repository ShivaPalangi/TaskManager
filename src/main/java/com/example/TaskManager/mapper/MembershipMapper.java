package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.MembershipDTO;
import com.example.TaskManager.entity.Membership;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.repository.UserRepository;
import com.example.TaskManager.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class MembershipMapper {
    private static TeamRepository teamRepository;
    private static UserRepository userRepository;

    public static MembershipDTO mapToMembershipDTO(Membership membership){
        if (membership == null) return null;

        MembershipDTO membershipDTO = new MembershipDTO();
        membershipDTO.setId(membership.getId());
        membershipDTO.setRole(membership.getRole());
        if ( membership.getTeam() != null )
            membershipDTO.setTeamId(membership.getTeam().getId());
        if ( membership.getEmployee() != null )
            membershipDTO.setEmployeeId(membership.getEmployee().getId());
        if ( membership.getAssignedTasks()!= null && !membership.getAssignedTasks().isEmpty() )
            membershipDTO.setAssignedTasks(membership.getAssignedTasks().stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList()));
        if ( membership.getCreatedTasks() != null && !membership.getCreatedTasks().isEmpty() )
            membershipDTO.setCreatedTasks(membership.getCreatedTasks().stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList()));
        return membershipDTO;
    }


    public static Membership mapToMembershipEntity(MembershipDTO membershipDTO){
        if (membershipDTO == null) return null;

        Membership membership = new Membership();
        membership.setId(membershipDTO.getId());
        membership.setRole(membershipDTO.getRole());
        if ( membershipDTO.getTeamId() != null )
            membership.setTeam(teamRepository.findById(membershipDTO.getTeamId()).orElseThrow(
                    () -> new ResourceNotFoundException("Team not found with id " + membershipDTO.getTeamId())));
        if ( membershipDTO.getEmployeeId() != null )
            membership.setEmployee(userRepository.findById(membershipDTO.getEmployeeId()).orElseThrow(
                    () -> new ResourceNotFoundException("user not found with id " + membershipDTO.getEmployeeId())));
        if ( membershipDTO.getAssignedTasks() != null && !membershipDTO.getAssignedTasks().isEmpty() )
            membership.setAssignedTasks(membershipDTO.getAssignedTasks().stream().map(TaskMapper::mapToTaskEntity).collect(Collectors.toList()));
        if ( membershipDTO.getCreatedTasks() != null && !membershipDTO.getCreatedTasks().isEmpty() )
            membership.setCreatedTasks(membershipDTO.getCreatedTasks().stream().map(TaskMapper::mapToTaskEntity).collect(Collectors.toList()));
        return membership;
    }
}