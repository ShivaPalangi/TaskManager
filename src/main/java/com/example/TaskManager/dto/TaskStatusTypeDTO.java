package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskStatusTypes;
import lombok.Data;
import java.util.List;

@Data
public class TaskStatusTypeDTO {
    private Long id;
    private String name;
    private String description;
    private TaskStatusTypes taskStatusType;
    private List<TaskStatusDTO> taskStatusList;

}
