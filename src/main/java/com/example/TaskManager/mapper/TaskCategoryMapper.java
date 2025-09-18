package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TaskCategoryDTO;
import com.example.TaskManager.entity.TaskCategory;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TaskCategoryMapper {
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskCategoryDTO mapToTaskCategoryDTO(TaskCategory taskCategory){
        if (taskCategory == null) return null;

        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO();
        taskCategoryDTO.setId(taskCategory.getId());
        taskCategoryDTO.setTitle(taskCategory.getTitle());
        if ( taskCategory.getTasks() != null && !taskCategory.getTasks().isEmpty() )
            taskCategoryDTO.setTasks(taskCategory.getTasks().stream().map(taskMapper::mapToTaskDTO).collect(Collectors.toList()));
        if ( taskCategory.getCreatedBy() != null )
            taskCategoryDTO.setCreatorId(taskCategory.getCreatedBy().getId());
        return taskCategoryDTO;
    }


    public TaskCategory mapToTaskCategoryEntity(TaskCategoryDTO taskCategoryDTO){
        if (taskCategoryDTO == null) return null;

        TaskCategory taskCategory = new TaskCategory();
        taskCategory.setId(taskCategoryDTO.getId());
        taskCategory.setTitle(taskCategoryDTO.getTitle());
        if ( taskCategoryDTO.getTasks() != null && !taskCategoryDTO.getTasks().isEmpty() )
            taskCategory.setTasks(taskCategoryDTO.getTasks().stream().map(taskMapper::mapToTaskEntity).collect(Collectors.toList()));
        if ( taskCategoryDTO.getCreatorId() != null )
            taskCategory.setCreatedBy(userRepository.findById(taskCategoryDTO.getCreatorId()).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with id " + taskCategoryDTO.getCreatorId())));
        return taskCategory;
    }
}