package com.example.TaskManager.dto;

import com.example.TaskManager.enums.MembershipRoles;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class MembershipDTO {
    private Long id;
    @NotNull(message = "Role is required", groups = ValidationGroups.Create.class)
    private MembershipRoles role;
    @NotNull(message = "Team Id is required", groups = ValidationGroups.Create.class)
    private Long teamId;
    @NotNull(message = "Employee Id is required", groups = ValidationGroups.Create.class)
    private Long employeeId;
    private List<TaskDTO> assignedTasks;
    private List<TaskDTO> createdTasks;
}