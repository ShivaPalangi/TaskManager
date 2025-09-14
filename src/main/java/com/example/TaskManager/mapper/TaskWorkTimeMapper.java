package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TaskWorkTimeDTO;
import com.example.TaskManager.entity.TaskWorkTime;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Component
public class TaskWorkTimeMapper {
    private static TaskRepository taskRepository;

    public static TaskWorkTimeDTO mapToTaskWorkTimeDTO(TaskWorkTime taskWorkTime){
        if (taskWorkTime == null) return null;

        TaskWorkTimeDTO taskWorkTimeDTO = new TaskWorkTimeDTO();
        taskWorkTimeDTO.setId(taskWorkTime.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        if ( taskWorkTime.getStartTime() != null )
            taskWorkTimeDTO.setStartTime(taskWorkTime.getStartTime().format(formatter));
        if ( taskWorkTime.getEndTime() != null )
            taskWorkTimeDTO.setEndTime(taskWorkTime.getEndTime().format(formatter));
        if ( taskWorkTime.getTask() != null )
            taskWorkTimeDTO.setTaskId(taskWorkTime.getTask().getId());
        return taskWorkTimeDTO;
    }


    public static TaskWorkTime mapToTaskWorkTimeEntity(TaskWorkTimeDTO taskWorkTimeDTO){
        if (taskWorkTimeDTO == null) return null;

        TaskWorkTime taskWorkTime = new TaskWorkTime();
        taskWorkTime.setId(taskWorkTimeDTO.getId());
        taskWorkTime.setStartTime(LocalDateTime.parse(taskWorkTimeDTO.getStartTime()));
        taskWorkTime.setEndTime(LocalDateTime.parse(taskWorkTimeDTO.getEndTime()));
        if ( taskWorkTimeDTO.getTaskId() != null )
            taskWorkTime.setTask(taskRepository.findById(taskWorkTimeDTO.getTaskId()).orElseThrow(
                    () -> new ResourceNotFoundException("Task not found with id " + taskWorkTimeDTO.getTaskId())));
        return taskWorkTime;
    }
}