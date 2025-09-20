package com.example.TaskManager.dto;

import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskWorkTimeDTO {
    private Long id;
    private String startTime;
    private String endTime;
//    @NotNull(message = "Task id is required", groups = ValidationGroups.Create.class)
    private Long taskId;
}