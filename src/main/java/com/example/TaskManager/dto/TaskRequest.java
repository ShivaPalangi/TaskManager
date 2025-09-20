package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskPriorities;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank(message = "Name is required", groups = ValidationGroups.Create.class)
    private String name;
    private String description;
    private String deadtime;
    private String duration;
    private String priority;
    @NotNull(message = "Responsible Id is required", groups = ValidationGroups.Create.class)
    private Long responsibleId;
    @NotNull(message = "Team Id is required", groups = ValidationGroups.Create.class)
    private Long teamId;


    public TaskPriorities getPriority() {
        if (priority == null)  return null;

        return switch (priority.toLowerCase().trim()) {
            case "low" -> TaskPriorities.LOW;
            case "medium" -> TaskPriorities.MEDIUM;
            case "high" -> TaskPriorities.HIGH;
            case "critical" -> TaskPriorities.CRITICAL;
            default -> throw new IllegalArgumentException("Invalid Priority" + priority);
        };
    }
}

