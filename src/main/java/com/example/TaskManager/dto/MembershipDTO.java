package com.example.TaskManager.dto;

import com.example.TaskManager.enums.MembershipRoles;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class MembershipDTO {
    private Long id;

    @NotNull(message = "موقعیت عضو نمی تواند خالی باشد")
    private String position;

    @NotNull(message = "نفش عضو نمی تواند خالی باشد")
    private MembershipRoles role;

    @NotNull(message = "آی دی شرکت نمی تواند خالی باشد")
    private TeamDTO team;

    @NotNull(message = "آی دی کارمند نمی تواند خالی باشد")
    private EmployeeDTO employee;

    private List<TaskDTO> assignedTasks;
    private List<TaskDTO> createdTasks;
}