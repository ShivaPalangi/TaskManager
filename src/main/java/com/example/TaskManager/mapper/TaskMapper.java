package com.example.TaskManager.mapper;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.entity.Membership;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.entity.TaskCategory;
import com.example.TaskManager.repository.MembershipRepository;
import com.example.TaskManager.repository.TaskCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class TaskMapper {
    private static TaskCategoryRepository taskCategoryRepository;
    private static MembershipRepository membershipRepository;

    public static TaskDTO mapToTaskDTO(Task task){
        if (task == null) return null;

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
        taskDTO.setTaskStatus(task.getTaskStatus());
        if (task.getTaskCategory() != null)
            taskDTO.setTaskCategoryId(task.getTaskCategory().getId());
        if (task.getResponsible() != null)
            taskDTO.setResponsibleId(task.getResponsible().getId());
        if (task.getCreatedBy() != null)
            taskDTO.setCreatorId(task.getCreatedBy().getId());
        if ( task.getWorkTimes() != null && !task.getWorkTimes().isEmpty())
            taskDTO.setWorkTimes(task.getWorkTimes().stream().map(TaskWorkTimeMapper::mapToTaskWorkTimeDTO).collect(Collectors.toList()));
        return taskDTO;
    }

    public static Task mapToTaskEntity(TaskDTO taskDTO){
        if (taskDTO == null) return null;

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
        task.setTaskStatus(taskDTO.getTaskStatus());
        if (taskDTO.getTaskCategoryId() != null){
            Optional<TaskCategory> taskCategory = taskCategoryRepository.findById(taskDTO.getTaskCategoryId());
            task.setTaskCategory(taskCategory.get());
        }
        if (taskDTO.getResponsibleId() != null){
            Optional<Membership> responsible = membershipRepository.findById(taskDTO.getResponsibleId());
            task.setResponsible(responsible.get());
        }
        if (taskDTO.getCreatorId() != null){
            Optional<Membership> creator = membershipRepository.findById(taskDTO.getCreatorId());
            task.setCreatedBy(creator.get());
        }
        if ( taskDTO.getWorkTimes() != null && !taskDTO.getWorkTimes().isEmpty())
            task.setWorkTimes(taskDTO.getWorkTimes().stream().map(TaskWorkTimeMapper::mapToTaskWorkTimeEntity).collect(Collectors.toList()));
        return task;
    }
}