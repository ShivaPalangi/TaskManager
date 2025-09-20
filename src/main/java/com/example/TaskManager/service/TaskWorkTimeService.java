package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskWorkTimeDTO;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.entity.TaskWorkTime;
import com.example.TaskManager.enums.TaskStatusTypes;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TaskWorkTimeMapper;
import com.example.TaskManager.repository.TaskRepository;
import com.example.TaskManager.repository.TaskWorkTimeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskWorkTimeService {
    private final TaskWorkTimeRepository taskWorkTimeRepository;
    private final TaskRepository taskRepository;
    private final TaskWorkTimeMapper taskWorkTimeMapper;


    @Transactional
    public Map<String, Object> startWorkTime(Long taskId) {
        if (taskWorkTimeRepository.existsByTaskIdAndEndTimeIsNull(taskId))
            throw new ResourceNotFoundException("There is already an active work session for this task");

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        if (TaskService.NOT_COMPLETED_STATUSES.contains(task.getTaskStatus())){
            TaskWorkTime taskWorkTime = new TaskWorkTime(task);
            taskWorkTime.setStartTime(LocalDateTime.now());
            taskWorkTimeRepository.save(taskWorkTime);
            task.setTaskStatus(TaskStatusTypes.IN_PROGRESS);
            taskRepository.save(task);
            return Map.of("message", "Work time has been started", "data", taskWorkTimeMapper.mapToTaskWorkTimeDTO(taskWorkTime));
        }
        else if ( task.getTaskStatus() ==  TaskStatusTypes.COMPLETED)
            return Map.of("message", "Task is already completed");
        else
            return Map.of("message", "You can't work on this task");
    }





    @Transactional
    public TaskWorkTimeDTO endActiveTaskWork(Long taskId) {
        TaskWorkTime taskWorkTime = taskWorkTimeRepository.findByTaskIdAndEndTimeIsNull(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("No active work session found for task id: " + taskId));
        taskWorkTime.setEndTime(LocalDateTime.now());
        taskWorkTimeRepository.save(taskWorkTime);
        return taskWorkTimeMapper.mapToTaskWorkTimeDTO(taskWorkTime);
    }




    public TaskWorkTimeDTO getActiveWorkSession(Long taskId) {
        TaskWorkTime workTime = taskWorkTimeRepository.findByTaskIdAndEndTimeIsNull(taskId).orElseThrow(
                () -> new ResourceNotFoundException("No active work session found for task id: " + taskId));
        return taskWorkTimeMapper.mapToTaskWorkTimeDTO(workTime);
    }




    public List<TaskWorkTimeDTO> getTaskWorkHistory(Long taskId) {
        List<TaskWorkTime> workTimes = taskWorkTimeRepository.findAllByTaskId(taskId);
        return workTimes
                .stream()
                .sorted(Comparator.comparing(TaskWorkTime::getStartTime).reversed())
                .map(taskWorkTimeMapper::mapToTaskWorkTimeDTO)
                .collect(Collectors.toList());
    }
}
