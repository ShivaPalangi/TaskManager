package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskPriorities;
import com.example.TaskManager.enums.TaskStatusTypes;
import lombok.Data;
import java.util.List;

@Data
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String deadtime;
    private String duration;
    private TaskPriorities priority;
    private TaskStatusTypes taskStatus;
    private Long taskCategoryId;
    private Long responsibleId;
    private Long creatorId;
    private List<TaskWorkTimeDTO> workTimes;
}