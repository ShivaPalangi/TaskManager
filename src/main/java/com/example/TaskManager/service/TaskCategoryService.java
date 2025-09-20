package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskCategoryDTO;
import com.example.TaskManager.entity.TaskCategory;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TaskCategoryMapper;
import com.example.TaskManager.mapper.TaskMapper;
import com.example.TaskManager.repository.TaskCategoryRepository;
import com.example.TaskManager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskCategoryService {
    private final TaskCategoryRepository taskCategoryRepository;
    private final TaskRepository taskRepository;
    private final TaskCategoryMapper taskCategoryMapper;
    private final TaskMapper taskMapper;


    @Transactional
    public TaskCategoryDTO addPrimaryTaskCategory(TaskCategoryDTO taskCategoryDTO, User user) {
        TaskCategory taskCategory = taskCategoryMapper.mapToTaskCategoryEntity(taskCategoryDTO);
        if ( !taskCategoryRepository.existsByTitleIgnoreCaseAndIsPrimaryTrue(taskCategory.getTitle()) ) {
            taskCategory.setIsPrimary(true);
            taskCategory.setCreatedBy(user);
            taskCategoryRepository.save(taskCategory);
            return taskCategoryMapper.mapToTaskCategoryDTO(taskCategory);
        }
        return null;
    }



    @Transactional
    public TaskCategoryDTO addTaskCategory(TaskCategoryDTO taskCategoryDTO, User user) {
        TaskCategory taskCategory = taskCategoryMapper.mapToTaskCategoryEntity(taskCategoryDTO);
        if ( !taskCategoryRepository.existsByCreatedByAndTitleIgnoreCase(user, taskCategory.getTitle()) ) {
            taskCategory.setIsPrimary(false);
            taskCategory.setCreatedBy(user);
            taskCategoryRepository.save(taskCategory);
            return taskCategoryMapper.mapToTaskCategoryDTO(taskCategory);
        }
        return null;
    }



    public List<TaskCategoryDTO> getAllPrimaryTaskCategories() {
        List<TaskCategory> taskCategories = taskCategoryRepository.findAllByIsPrimaryTrue();
        return taskCategories
                .stream()
                .sorted(Comparator.comparing(TaskCategory::getTitle))
                .map(taskCategoryMapper::mapToTaskCategoryDTO)
                .collect(Collectors.toList());
    }



    public List<TaskCategoryDTO> getAllUserTaskCategories(User user) {
        List<TaskCategory> taskCategories = taskCategoryRepository.findAllByIsPrimaryTrueOrCreatedBy(user);
        return taskCategories
                .stream()
                .sorted(Comparator.comparing(TaskCategory::getTitle))
                .map(taskCategoryMapper::mapToTaskCategoryDTO)
                .collect(Collectors.toList());
    }



    public List<TaskCategoryDTO> findCategoryByTitle(String title, User  user) {
        List<TaskCategory> allUserCategories = taskCategoryRepository.findAllByIsPrimaryTrueOrCreatedBy(user);
        return allUserCategories
                .stream()
                .filter(category -> category.getTitle().toLowerCase().contains(title.toLowerCase()))
                .sorted(Comparator.comparing(TaskCategory::getTitle))
                .map(taskCategoryMapper::mapToTaskCategoryDTO)
                .collect(Collectors.toList());
    }



    public List<TaskCategoryDTO> findPrimaryCategoryByTitle(String title) {
        List<TaskCategory> categories = taskCategoryRepository.findAllByIsPrimaryTrueAndTitleContainingIgnoreCase(title);
        return categories
                .stream()
                .sorted(Comparator.comparing(TaskCategory::getTitle))
                .map(taskCategoryMapper::mapToTaskCategoryDTO)
                .collect(Collectors.toList());
    }



    public TaskCategoryDTO getTaskCategoryById(Long id, User user) {
        TaskCategory category = taskCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Task category with id %d not found!".formatted(id)));
        return showUserOwnTasks(user, category);
    }



    public TaskCategoryDTO getPrimaryTaskCategoryById(Long id) {
        TaskCategory category = taskCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("TaskCategory with id %d not found!".formatted(id)));
        return setTaskListEmpty(category);
    }



    @Transactional
    public TaskCategoryDTO updatePrimaryTaskCategory(Long id, TaskCategoryDTO taskCategoryDTO) {
        TaskCategory category = taskCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("TaskCategory with id %d not found!".formatted(id)));
        category.setTitle(taskCategoryDTO.getTitle().trim());
        taskCategoryRepository.save(category);
        return setTaskListEmpty(category);
    }



    @Transactional
    public TaskCategoryDTO updateTaskCategory(Long id, TaskCategoryDTO taskCategoryDTO, User user) {
        TaskCategory taskCategory = taskCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("TaskCategory with id %d not found!".formatted(id)));
        taskCategory.setTitle(taskCategoryDTO.getTitle().trim());
        taskCategoryRepository.save(taskCategory);
        return showUserOwnTasks(user, taskCategory);
    }



    @Transactional
    public void deleteTaskCategory(Long id) {
        taskCategoryRepository.deleteById(id);
    }



    private TaskCategoryDTO setTaskListEmpty(TaskCategory category) {
        TaskCategoryDTO categoryDTO = taskCategoryMapper.mapToTaskCategoryDTO(category);
        categoryDTO.setTasks(null);
        return categoryDTO;
    }

    private TaskCategoryDTO showUserOwnTasks(User user, TaskCategory category) {
        TaskCategoryDTO categoryDTO = taskCategoryMapper.mapToTaskCategoryDTO(category);
        categoryDTO.setTasks(taskRepository.findAllByTaskCategoryAndResponsibleEmployee(category, user)
                .stream()
                .map(taskMapper::mapToTaskDTO)
                .collect(Collectors.toList()));
        return  categoryDTO;
    }
}
