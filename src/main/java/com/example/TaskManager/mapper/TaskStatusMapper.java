package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TaskStatusDTO;
import com.example.TaskManager.entity.TaskStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TaskStatusMapper {
    public static TaskStatusDTO mapToTaskStatusDTO(TaskStatus taskStatus){
        TaskStatusDTO taskStatusDTO = new TaskStatusDTO();
        taskStatusDTO.setId(taskStatus.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        if (taskStatus.getCreatedDate() != null)
            taskStatusDTO.setCreatedDate(taskStatus.getCreatedDate().format(formatter));
        if (taskStatus.getDeactivatedTime() != null)
            taskStatusDTO.setDeactivatedTime(taskStatus.getDeactivatedTime().format(formatter));
        if (taskStatus.getTaskStatusType() != null)
            taskStatusDTO.setTaskStatusType(TaskStatusTypeMapper.mapToTaskStatusTypeDTO(taskStatus.getTaskStatusType()));
        if (taskStatus.getTask() != null)
            taskStatusDTO.setTask(TaskMapper.mapToTaskDTO(taskStatus.getTask()));
        if ( taskStatus.getTasks() != null && !taskStatus.getTasks().isEmpty())
            taskStatusDTO.setTasks(taskStatus.getTasks().stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList()));
        return taskStatusDTO;
    }

    public static TaskStatus mapToTaskStatusEntity(TaskStatusDTO taskStatusDTO){
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setId(taskStatusDTO.getId());
        if (taskStatusDTO.getCreatedDate() != null)
            taskStatus.setCreatedDate(LocalDateTime.parse(taskStatusDTO.getCreatedDate()));
        if (taskStatusDTO.getDeactivatedTime() != null)
            taskStatus.setDeactivatedTime(LocalDateTime.parse(taskStatusDTO.getDeactivatedTime()));
        if (taskStatusDTO.getTaskStatusType() != null)
            taskStatus.setTaskStatusType(TaskStatusTypeMapper.mapToTaskStatusTypeEntity(taskStatusDTO.getTaskStatusType()));
        if (taskStatusDTO.getTask() != null)
            taskStatus.setTask(TaskMapper.mapToTaskEntity(taskStatusDTO.getTask()));
        if ( taskStatusDTO.getTasks() != null && !taskStatusDTO.getTasks().isEmpty())
            taskStatus.setTasks(taskStatusDTO.getTasks().stream().map(TaskMapper::mapToTaskEntity).collect(Collectors.toList()));
        return taskStatus;
    }
}