package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.MembershipDTO;
import com.example.TaskManager.entity.Membership;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class MembershipMapper {

    public static MembershipDTO mapToMembershipDTO(Membership membership){
        MembershipDTO membershipDTO = new MembershipDTO();
        membershipDTO.setId(membership.getId());
        membershipDTO.setPosition(membership.getPosition());
        membershipDTO.setRole(membership.getRole());
        if (membership.getTeam() != null)
            membershipDTO.setTeam(TeamMapper.mapToTeamDTO(membership.getTeam()));
        if (membership.getEmployee() != null)
            membershipDTO.setEmployee(EmployeeMapper.mapToEmployeeDTO(membership.getEmployee()));
        if ( membership.getAssignedTasks()!= null && !membership.getAssignedTasks().isEmpty())
            membershipDTO.setAssignedTasks(membership.getAssignedTasks().stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList()));
        if ( membership.getCreatedTasks() != null && !membership.getCreatedTasks().isEmpty())
            membershipDTO.setCreatedTasks(membership.getCreatedTasks().stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList()));
        return membershipDTO;
    }


    public static Membership mapToMembershipEntity(MembershipDTO membershipDTO){
        Membership membership = new Membership();
        membership.setId(membershipDTO.getId());
        membership.setPosition(membershipDTO.getPosition());
        membership.setRole(membershipDTO.getRole());
        if (membershipDTO.getTeam() != null)
            membership.setTeam(TeamMapper.mapToTeamEntity(membershipDTO.getTeam()));
        if (membershipDTO.getEmployee() != null)
            membership.setEmployee(EmployeeMapper.mapToEmployeeEntity(membershipDTO.getEmployee()));
        if ( membershipDTO.getAssignedTasks() != null && !membershipDTO.getAssignedTasks().isEmpty())
            membership.setAssignedTasks(membershipDTO.getAssignedTasks().stream().map(TaskMapper::mapToTaskEntity).collect(Collectors.toList()));
        if ( membershipDTO.getCreatedTasks() != null && !membershipDTO.getCreatedTasks().isEmpty())
            membership.setCreatedTasks(membershipDTO.getCreatedTasks().stream().map(TaskMapper::mapToTaskEntity).collect(Collectors.toList()));
        return membership;
    }
}