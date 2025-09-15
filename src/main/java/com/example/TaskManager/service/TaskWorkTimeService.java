package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskWorkTimeDTO;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.entity.TaskWorkTime;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TaskWorkTimeMapper;
import com.example.TaskManager.repository.TaskRepository;
import com.example.TaskManager.repository.TaskWorkTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskWorkTimeService {
    private final TaskWorkTimeRepository taskWorkTimeRepository;
    private final TaskRepository taskRepository;


    public TaskWorkTimeDTO startWorkTime(TaskWorkTimeDTO taskWorkTimeDTO) {
        Task task = taskRepository.findById(taskWorkTimeDTO.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskWorkTimeDTO.getTaskId()));
        if (taskWorkTimeRepository.existsByTaskIdAndEndTimeIsNull(taskWorkTimeDTO.getTaskId()))
            throw new RuntimeException("There is already an active work session for this task");
        TaskWorkTime taskWorkTime = new TaskWorkTime(task);
        taskWorkTimeRepository.save(taskWorkTime);
        return TaskWorkTimeMapper.mapToTaskWorkTimeDTO(taskWorkTime);
    }


    public TaskWorkTimeDTO endActiveTaskWork(Long taskId) {
        TaskWorkTime taskWorkTime = taskWorkTimeRepository.findFirstByTaskIdAndEndTimeIsNullOrderByStartTimeDesc(taskId)
                .orElseThrow(() -> new RuntimeException("No active work session found for task id: " + taskId));
        taskWorkTime.setEndTime(LocalDateTime.now());
        taskWorkTimeRepository.save(taskWorkTime);
        return TaskWorkTimeMapper.mapToTaskWorkTimeDTO(taskWorkTime);
    }


    public TaskWorkTimeDTO getActiveWorkSession(Long taskId) {
        TaskWorkTime workTime = taskWorkTimeRepository.findFirstByTaskIdAndEndTimeIsNullOrderByStartTimeDesc(taskId).orElseThrow(
                () -> new ResourceNotFoundException("No active work session found for task id: " + taskId));
        return TaskWorkTimeMapper.mapToTaskWorkTimeDTO(workTime);
    }


    public List<TaskWorkTimeDTO> getTaskWorkHistory(Long taskId) {
        List<TaskWorkTime> workTimes = taskWorkTimeRepository.findAllByTaskIdOrderByStartTimeDesc(taskId);
        return workTimes.stream().map(TaskWorkTimeMapper::mapToTaskWorkTimeDTO).collect(Collectors.toList());
    }
}
