package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskPriorities;
import com.example.TaskManager.enums.TaskStatusTypes;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class TaskDTO {
    private Long id;

    @NotBlank(message = "Name is required", groups = ValidationGroups.Create.class)
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String name;

    private String description;
    private String startTime;
    private String endTime;
    private String deadtime;
    private String duration;
    private TaskPriorities priority;
    private TaskStatusTypes taskStatus;
    private Long taskCategoryId;

    @NotNull(message = "Responsible Id is required", groups = ValidationGroups.Create.class)
    private Long responsibleId;

    @NotNull(message = "Creator Id is required", groups = ValidationGroups.Create.class)
    private Long creatorId;

    private List<TaskWorkTimeDTO> workTimes;
}