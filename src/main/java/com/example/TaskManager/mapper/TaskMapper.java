package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.entity.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TaskMapper {
    public static TaskDTO mapToTaskDTO(Task task){
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setDescription(task.getDescription());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        if (task.getStartTime() != null)
            taskDTO.setStartTime(task.getStartTime().format(formatter));
        if (task.getEndTime() != null)
            taskDTO.setEndTime(task.getEndTime().format(formatter));
        if (task.getDeadtime() != null)
            taskDTO.setDeadtime(task.getDeadtime().format(formatter));
        if (task.getDuration() != null)
            taskDTO.setDuration(task.getDuration().format(formatter));
        taskDTO.setPriority(task.getPriority());
        if (task.getTaskCategory() != null)
            taskDTO.setTaskCategory(TaskCategoryMapper.mapToTaskCategoryDTO(task.getTaskCategory()));
        if (task.getResponsible() != null)
            taskDTO.setResponsible(MembershipMapper.mapToMembershipDTO(task.getResponsible()));
        if (task.getCreatedBy() != null)
            taskDTO.setCreatedBy(MembershipMapper.mapToMembershipDTO(task.getCreatedBy()));
        if ( ! task.getWorkTimes().isEmpty())
            taskDTO.setWorkTimes(task.getWorkTimes().stream().map(TaskWorkTimeMapper::mapToTaskWorkTimeDTO).collect(Collectors.toList()));
        if ( ! task.getTaskStatusHistory().isEmpty())
            taskDTO.setTaskStatusHistory(task.getTaskStatusHistory().stream().map(TaskStatusMapper::mapToTaskStatusDTO).collect(Collectors.toList()));
        if (task.getTaskActiveStatus() != null)
            taskDTO.setTaskActiveStatus(TaskStatusMapper.mapToTaskStatusDTO(task.getTaskActiveStatus()));
        return taskDTO;
    }

    public static Task mapToTaskEntity(TaskDTO taskDTO){
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        if (taskDTO.getStartTime() != null)
            task.setStartTime(LocalDateTime.parse(taskDTO.getStartTime()));
        if (taskDTO.getEndTime() != null)
            task.setEndTime(LocalDateTime.parse(taskDTO.getEndTime()));
        if (taskDTO.getDeadtime() != null)
            task.setDeadtime(LocalDateTime.parse(taskDTO.getDeadtime()));
        if (taskDTO.getDuration() != null)
            task.setDuration(LocalDateTime.parse(taskDTO.getDuration()));
        task.setPriority(taskDTO.getPriority());
        if (taskDTO.getTaskCategory() != null)
            task.setTaskCategory(TaskCategoryMapper.mapToTaskCategoryEntity(taskDTO.getTaskCategory()));
        if (taskDTO.getResponsible() != null)
            task.setResponsible(MembershipMapper.mapToMembershipEntity(taskDTO.getResponsible()));
        if (taskDTO.getCreatedBy() != null)
            task.setCreatedBy(MembershipMapper.mapToMembershipEntity(taskDTO.getCreatedBy()));
        if ( ! taskDTO.getWorkTimes().isEmpty())
            task.setWorkTimes(taskDTO.getWorkTimes().stream().map(TaskWorkTimeMapper::mapToTaskWorkTimeEntity).collect(Collectors.toList()));
        if ( ! taskDTO.getTaskStatusHistory().isEmpty())
            task.setTaskStatusHistory(taskDTO.getTaskStatusHistory().stream().map(TaskStatusMapper::mapToTaskStatusEntity).collect(Collectors.toList()));
        if (taskDTO.getTaskActiveStatus() != null)
            task.setTaskActiveStatus(TaskStatusMapper.mapToTaskStatusEntity(taskDTO.getTaskActiveStatus()));
        return task;
    }
}