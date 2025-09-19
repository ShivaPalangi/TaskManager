package com.example.TaskManager.dto;

import com.example.TaskManager.enums.MembershipRoles;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class MembershipDTO {
    private Long id;
    private MembershipRoles role;
    private Long teamId;
    @NotNull(message = "Employee Id is required", groups = ValidationGroups.Create.class)
    private Long employeeId;
    private List<TaskDTO> assignedTasks;
    private List<TaskDTO> createdTasks;
}