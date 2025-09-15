package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskCategoryDTO;
import com.example.TaskManager.entity.TaskCategory;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TaskCategoryMapper;
import com.example.TaskManager.mapper.TaskMapper;
import com.example.TaskManager.repository.TaskCategoryRepository;
import com.example.TaskManager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskCategoryService {
    private final TaskCategoryRepository taskCategoryRepository;
    private final TaskRepository taskRepository;


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



    public TaskCategoryDTO addTaskCategory(TaskCategoryDTO taskCategoryDTO, User user) {
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



    public TaskCategoryDTO getTaskCategoryById(Long id, User user) {
        TaskCategory category = taskCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task category with id %d not found!".formatted(id)));
        TaskCategoryDTO categoryDTO = TaskCategoryMapper.mapToTaskCategoryDTO(category);
        categoryDTO.setTasks(taskRepository.findAllByTaskCategoryAndResponsibleEmployee(category, user)
                .stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList()));
        return categoryDTO;
    }



    public TaskCategoryDTO getPrimaryTaskCategoryById(Long id) {
        TaskCategory category = taskCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("TaskCategory with id %d not found!".formatted(id)));
        return setTaskListEmpty(category);
    }



    public TaskCategoryDTO updatePrimaryTaskCategory(Long id, TaskCategoryDTO taskCategoryDTO) {
        TaskCategory category = taskCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("TaskCategory with id %d not found!".formatted(id)));
        category.setTitle(taskCategoryDTO.getTitle().trim());
        taskCategoryRepository.save(category);
        return setTaskListEmpty(category);
    }



    public TaskCategoryDTO updateTaskCategory(Long id, TaskCategoryDTO taskCategoryDTO) {
        TaskCategory taskCategory = taskCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("TaskCategory with id %d not found!".formatted(id)));
        taskCategory.setTitle(taskCategoryDTO.getTitle().trim());
        taskCategoryRepository.save(taskCategory);
        return TaskCategoryMapper.mapToTaskCategoryDTO(taskCategory);
    }



    public String deleteTaskCategory(Long id) {
        taskCategoryRepository.deleteById(id);
        return "Task category successfully deleted.";
    }



    private TaskCategoryDTO setTaskListEmpty(TaskCategory category) {
        TaskCategoryDTO categoryDTO = TaskCategoryMapper.mapToTaskCategoryDTO(category);
        categoryDTO.setTasks(null);
        return categoryDTO;
    }
}
