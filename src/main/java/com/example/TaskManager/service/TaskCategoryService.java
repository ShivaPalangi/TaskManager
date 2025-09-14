package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskCategoryDTO;
import com.example.TaskManager.entity.TaskCategory;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.mapper.TaskCategoryMapper;
import com.example.TaskManager.repository.TaskCategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskCategoryService {
    private final TaskCategoryRepository taskCategoryRepository;

    public TaskCategoryDTO addPrimaryTaskCategory(TaskCategoryDTO taskCategoryDTO, User user) {
        TaskCategory taskCategory = TaskCategoryMapper.mapToTaskCategoryEntity(taskCategoryDTO);
        if ( !taskCategoryRepository.existsByTitleIgnoreCaseAndIsPrimaryTrue(taskCategory.getTitle()) ) {
            taskCategory.setIsPrimary(true);
            taskCategory.setCreatedBy(user);
            taskCategoryRepository.save(taskCategory);
            return TaskCategoryMapper.mapToTaskCategoryDTO(taskCategory);
        }
        return null;
    }

    public TaskCategoryDTO addTaskCategory(@Valid TaskCategoryDTO taskCategoryDTO, User user) {
        TaskCategory taskCategory = TaskCategoryMapper.mapToTaskCategoryEntity(taskCategoryDTO);
        if ( !taskCategoryRepository.existsByCreatedByAndTitleIgnoreCase(user, taskCategory.getTitle()) ) {
            taskCategory.setIsPrimary(false);
            taskCategory.setCreatedBy(user);
            taskCategoryRepository.save(taskCategory);
            return TaskCategoryMapper.mapToTaskCategoryDTO(taskCategory);
        }
        return null;
    }

    public List<TaskCategoryDTO> getAllPrimaryTaskCategories() {
        List<TaskCategory> taskCategories = taskCategoryRepository.findAllByIsPrimaryTrue();
        return taskCategories.stream().map(TaskCategoryMapper::mapToTaskCategoryDTO).collect(Collectors.toList());
    }

    public List<TaskCategoryDTO> getAllUserTaskCategories(User user) {
        List<TaskCategory> taskCategories = taskCategoryRepository.findAllByIsPrimaryTrueOrCreatedBy(user);
        return taskCategories.stream().map(TaskCategoryMapper::mapToTaskCategoryDTO).collect(Collectors.toList());
    }

    public List<TaskCategoryDTO> findCategoryByTitle(String title, User  user) {
        List<TaskCategory> categories = taskCategoryRepository.findCategoriesByTitleAndUserOrPrimary(title, user);
        return categories.stream().map(TaskCategoryMapper::mapToTaskCategoryDTO).collect(Collectors.toList());
    }

    public List<TaskCategoryDTO> findPrimaryCategoryByTitle(String title) {
        List<TaskCategory> categories = taskCategoryRepository.findAllByIsPrimaryTrueAndTitleContainingIgnoreCase(title);
        return categories.stream().map(TaskCategoryMapper::mapToTaskCategoryDTO).collect(Collectors.toList());
    }
}
