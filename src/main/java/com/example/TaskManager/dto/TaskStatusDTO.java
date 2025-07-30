package com.example.TaskManager.dto;

import lombok.Data;
import java.util.List;

@Data
public class TaskStatusDTO {
    private Long id;
    private String createdDate;
    private String deactivatedTime;
    private TaskStatusTypeDTO taskStatusType;
    private TaskDTO task;
    private List<TaskDTO> tasks;
}