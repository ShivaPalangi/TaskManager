package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.entity.Membership;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.enums.TaskStatusTypes;
import com.example.TaskManager.exception.BadRequestException;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TaskMapper;
import com.example.TaskManager.repository.MembershipRepository;
import com.example.TaskManager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final MembershipRepository membershipRepository;

    public static final EnumSet<TaskStatusTypes> NOT_COMPLETED_STATUSES =
            EnumSet.of(TaskStatusTypes.NOT_STARTED, TaskStatusTypes.IN_PROGRESS);



    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task taskToUpdate = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        updateTaskEntityFromDTO(taskDTO, taskToUpdate);
        taskRepository.save(taskToUpdate);
        return TaskMapper.mapToTaskDTO(taskToUpdate);

    }



    private void updateTaskEntityFromDTO(TaskDTO taskDTO, Task task) {
        if ( !taskDTO.getName().isBlank())
            task.setName(taskDTO.getName());
        if ( !taskDTO.getDescription().isBlank())
            task.setDescription(taskDTO.getDescription());
        if ( taskDTO.getPriority() != null)
            task.setPriority(taskDTO.getPriority());
        if ( taskDTO.getResponsibleId() != null )
            task.setResponsible(membershipRepository.findByEmployeeIdAndTeamId(
                    taskDTO.getResponsibleId(), task.getCreatedBy().getTeam().getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Responsible not found with id " + taskDTO.getResponsibleId())));
        if ( !taskDTO.getDeadtime().isBlank())
            task.setDeadtime(LocalDateTime.parse(taskDTO.getDeadtime()));
        if ( !taskDTO.getDuration().isBlank())
            task.setDuration(LocalDateTime.parse(taskDTO.getDuration()));
    }



    public void deleteTask(Long taskId) {
        Task taskToDelete = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        if (taskToDelete.getTaskStatus() == TaskStatusTypes.DRAFT)
            taskRepository.delete(taskToDelete);
        else {
            taskToDelete.setTaskStatus(TaskStatusTypes.DELETED);
            taskRepository.save(taskToDelete);
        }
    }



    public void cancelTask(Long taskId) {
        Task taskToCancel = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        if (taskToCancel.getTaskStatus() == TaskStatusTypes.DRAFT)
            taskRepository.delete(taskToCancel);
        else {
            taskToCancel.setTaskStatus(TaskStatusTypes.CANCELED);
            taskRepository.save(taskToCancel);
        }
    }



    public List<TaskDTO> getAllTasks(User user) {
        List<Task> tasks = taskRepository.findAllByResponsibleEmployee(user);
        List<Task> filteredTasks = tasks.stream()
                .filter(task -> NOT_COMPLETED_STATUSES.contains(task.getTaskStatus()))
                .sorted(Comparator.comparing(Task::getStartTime))
                .toList();
        return filteredTasks.stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList());
    }



    public List<TaskDTO> getAllTasksHistory(User user) {
        List<Task> tasks = taskRepository.findAllByResponsibleEmployee(user);
        return tasks
                .stream()
                .filter(task -> task.getTaskStatus() != TaskStatusTypes.DRAFT)
                .sorted(Comparator.comparing(Task::getStartTime))
                .map(TaskMapper::mapToTaskDTO)
                .collect(Collectors.toList());
    }


    public List<TaskDTO> assignTasks(User user) {
        List<Task> tasks = taskRepository.findAllByCreatedByEmployee(user);
        List<Task> draftTasks = tasks.stream().filter(task -> task.getTaskStatus() == TaskStatusTypes.DRAFT).toList();
        for (Task task : draftTasks) {
            task.setTaskStatus(TaskStatusTypes.NOT_STARTED);
            taskRepository.save(task);
        }
         return draftTasks.stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList());
    }

    public List<TaskDTO> getDraftTasks(User user) {
        List<Task> tasks = taskRepository.findAllByCreatedByEmployee(user);
        return tasks
                .stream()
                .filter(task -> task.getTaskStatus() == TaskStatusTypes.DRAFT)
                .sorted(Comparator.comparing(Task::getStartTime).reversed())
                .map(TaskMapper::mapToTaskDTO)
                .collect(Collectors.toList());
    }


    public TaskDTO completeTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        if (task.getTaskStatus() == TaskStatusTypes.COMPLETED)
            throw new BadRequestException("This task is already completed");
        task.setTaskStatus(TaskStatusTypes.COMPLETED);
        task.setEndTime(LocalDateTime.now());
        taskRepository.save(task);
        return TaskMapper.mapToTaskDTO(task);
    }

    public List<TaskDTO> sortTasksByPriority(User user) {
        List<Task> tasks = taskRepository.findAllByResponsibleEmployee(user);

        return tasks.stream()
                .filter(task -> NOT_COMPLETED_STATUSES.contains(task.getTaskStatus()))
                .sorted(Comparator.comparing(task -> {
                    if (task.getPriority() == null)
                        return Integer.MAX_VALUE;  // null هارو میبره آخر

                    return switch (task.getPriority()) {
                        case CRITICAL -> 1;
                        case HIGH -> 2;
                        case MEDIUM -> 3;
                        case LOW -> 4;
                    };
                }, Comparator.naturalOrder()))
                .map(TaskMapper::mapToTaskDTO)
                .toList();
    }


    public List<TaskDTO> sortTasksByDeadTime(User user) {
        List<Task> tasks = taskRepository.findAllByResponsibleEmployee(user);
        return tasks.stream()
                .filter(task -> NOT_COMPLETED_STATUSES.contains(task.getTaskStatus()))
                .sorted(Comparator.comparing(Task::getDeadtime))
                .map(TaskMapper::mapToTaskDTO)
                .toList();
    }


    public TaskDTO getTaskById(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        if ( !task.getCreatedBy().getEmployee().equals(user) && task.getTaskStatus() == TaskStatusTypes.DRAFT)
            throw new ResourceNotFoundException("Task with id %d not found".formatted(taskId));
        return TaskMapper.mapToTaskDTO(task);
    }

    public Object addTask(TaskDTO taskDTO, Long teamId, User user) {
        if ( membershipRepository.existsByEmployeeIdAndTeamId(taskDTO.getResponsibleId(), teamId) ){
            Task task = TaskMapper.mapToTaskEntity(taskDTO);
            Membership createdBy = membershipRepository.findByEmployeeAndTeamId(user, teamId).orElseThrow(
                    () -> new ResourceNotFoundException("Membership with id %d not found".formatted(teamId)));
            task.setCreatedBy(createdBy);
            taskRepository.save(task);
            return TaskMapper.mapToTaskDTO(task);
        }
        return "The membership with id %d is not the member of this team";
    }
}