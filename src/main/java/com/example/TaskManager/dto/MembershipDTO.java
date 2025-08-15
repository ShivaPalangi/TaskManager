package com.example.TaskManager.dto;

import com.example.TaskManager.enums.MembershipRoles;
import lombok.Data;
import java.util.List;

@Data
public class MembershipDTO {
    private Long id;
    private String position;
    private MembershipRoles role;
    private Long teamId;
    private Long employeeId;
    private List<TaskDTO> assignedTasks;
    private List<TaskDTO> createdTasks;
}