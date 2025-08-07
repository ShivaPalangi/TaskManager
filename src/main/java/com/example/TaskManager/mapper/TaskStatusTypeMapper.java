package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TaskStatusTypeDTO;
import com.example.TaskManager.entity.TaskStatusType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TaskStatusTypeMapper {

    public static TaskStatusTypeDTO mapToTaskStatusTypeDTO(TaskStatusType taskStatusType){
        TaskStatusTypeDTO taskStatusTypeDTO = new TaskStatusTypeDTO();
        taskStatusTypeDTO.setId(taskStatusType.getId());
        taskStatusTypeDTO.setName(taskStatusType.getName());
        taskStatusTypeDTO.setDescription(taskStatusType.getDescription());
        taskStatusTypeDTO.setTaskStatusType(taskStatusType.getTaskStatusType());
        if ( taskStatusType.getTaskStatusList() != null && !taskStatusType.getTaskStatusList().isEmpty())
            taskStatusTypeDTO.setTaskStatusList(taskStatusType.getTaskStatusList().stream().map(TaskStatusMapper::mapToTaskStatusDTO).collect(Collectors.toList()));
        return taskStatusTypeDTO;
    }


    public static TaskStatusType mapToTaskStatusTypeEntity(TaskStatusTypeDTO taskStatusTypeDTO){
        TaskStatusType taskStatusType = new TaskStatusType();
        taskStatusType.setId(taskStatusTypeDTO.getId());
        taskStatusType.setName(taskStatusTypeDTO.getName());
        taskStatusType.setDescription(taskStatusTypeDTO.getDescription());
        taskStatusType.setTaskStatusType(taskStatusTypeDTO.getTaskStatusType());
        if ( taskStatusTypeDTO.getTaskStatusList() != null && !taskStatusTypeDTO.getTaskStatusList().isEmpty())
            taskStatusType.setTaskStatusList(taskStatusTypeDTO.getTaskStatusList().stream().map(TaskStatusMapper::mapToTaskStatusEntity).collect(Collectors.toList()));
        return taskStatusType;
    }
}