package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TaskCategoryDTO;
import com.example.TaskManager.entity.TaskCategory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TaskCategoryMapper {

    public static TaskCategoryDTO mapToTaskCategoryDTO(TaskCategory taskCategory){
        if (taskCategory == null) return null;

        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO();
        taskCategoryDTO.setId(taskCategory.getId());
        taskCategoryDTO.setCategory(taskCategory.getCategory());
        if ( taskCategory.getTasks() != null && !taskCategory.getTasks().isEmpty())
            taskCategoryDTO.setTasks(taskCategory.getTasks().stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList()));
        return taskCategoryDTO;
    }


    public static TaskCategory mapToTaskCategoryEntity(TaskCategoryDTO taskCategoryDTO){
        if (taskCategoryDTO == null) return null;

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setId(taskCategoryDTO.getId());
        taskCategory.setCategory(taskCategoryDTO.getCategory());
        if ( taskCategoryDTO.getTasks() != null && !taskCategoryDTO.getTasks().isEmpty())
            taskCategory.setTasks(taskCategoryDTO.getTasks().stream().map(TaskMapper::mapToTaskEntity).collect(Collectors.toList()));
        return taskCategory;
    }
}