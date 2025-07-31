package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TaskWorkTimeDTO;
import com.example.TaskManager.entity.TaskWorkTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Component
public class TaskWorkTimeMapper {

    public static TaskWorkTimeDTO mapToTaskWorkTimeDTO(TaskWorkTime taskWorkTime){
        TaskWorkTimeDTO taskWorkTimeDTO = new TaskWorkTimeDTO();
        taskWorkTimeDTO.setId(taskWorkTime.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        if (taskWorkTime.getStartTime() != null)
            taskWorkTimeDTO.setStartTime(taskWorkTime.getStartTime().format(formatter));
        if (taskWorkTime.getEndTime() != null)
            taskWorkTimeDTO.setEndTime(taskWorkTime.getEndTime().format(formatter));
        if (taskWorkTime.getTask() != null)
            taskWorkTimeDTO.setTask(TaskMapper.mapToTaskDTO(taskWorkTime.getTask()));
        return taskWorkTimeDTO;
    }


    public static TaskWorkTime mapToTaskWorkTimeEntity(TaskWorkTimeDTO taskWorkTimeDTO){
        TaskWorkTime taskWorkTime = new TaskWorkTime();
        taskWorkTime.setId(taskWorkTimeDTO.getId());
        taskWorkTime.setStartTime(LocalDateTime.parse(taskWorkTimeDTO.getStartTime()));
        taskWorkTime.setEndTime(LocalDateTime.parse(taskWorkTimeDTO.getEndTime()));
        if (taskWorkTimeDTO.getTask() != null)
            taskWorkTime.setTask(TaskMapper.mapToTaskEntity(taskWorkTimeDTO.getTask()));
        return taskWorkTime;
    }
}