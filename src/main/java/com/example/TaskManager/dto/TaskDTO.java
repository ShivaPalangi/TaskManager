package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskPriorities;
import com.example.TaskManager.enums.TaskStatusTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class TaskDTO {
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String deadtime;
    private String duration;
    private TaskPriorities priority;
    private TaskStatusTypes taskStatus;
    private Long taskCategoryId;
    @NotNull(message = "Responsible Id is required")
    private Long responsibleId;
    private Long creatorId;
    private List<TaskWorkTimeDTO> workTimes;
}