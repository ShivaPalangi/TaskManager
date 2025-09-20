package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.dto.TaskRequest;
import com.example.TaskManager.dto.TaskRequestByResponsible;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.entity.TaskWorkTime;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.enums.TaskStatusTypes;
import com.example.TaskManager.exception.ResourceNotFoundException;
import com.example.TaskManager.mapper.TaskMapper;
import com.example.TaskManager.repository.MembershipRepository;
import com.example.TaskManager.repository.TaskCategoryRepository;
import com.example.TaskManager.repository.TaskRepository;
import com.example.TaskManager.repository.TaskWorkTimeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final MembershipRepository membershipRepository;
    private final TaskCategoryRepository taskCategoryRepository;
    private final PermissionService permissionService;
    private final TaskWorkTimeRepository taskWorkTimeRepository;

    public static final EnumSet<TaskStatusTypes> NOT_COMPLETED_STATUSES =
            EnumSet.of(TaskStatusTypes.NOT_STARTED, TaskStatusTypes.IN_PROGRESS);




    @Transactional
    public TaskDTO addTask(TaskRequest taskRequest, User user) {
        Task task = new Task();
        task.setName(taskRequest.getName());
        task.setDescription(taskRequest.getDescription());
        task.setTaskStatus(TaskStatusTypes.DRAFT);
        task.setCreatedBy(membershipRepository.findByEmployeeAndTeamId(user, taskRequest.getTeamId()).get());
        task.setResponsible(membershipRepository.findByIdAndTeamId(taskRequest.getResponsibleId(), taskRequest.getTeamId()).orElseThrow(
                () -> new ResourceNotFoundException("The membership with id %d is not the member of this team".formatted(taskRequest.getResponsibleId()))));
        task.setDeadtime(taskRequest.getDeadtime() != null && !taskRequest.getDeadtime().isBlank() ?
                LocalDateTime.parse(taskRequest.getDeadtime()) : null );
        task.setDuration(taskRequest.getDuration() != null && !taskRequest.getDuration().isBlank() ?
                LocalDateTime.parse(taskRequest.getDuration()) : null);
        task.setPriority(taskRequest.getPriority());
        task.setStartTime(LocalDateTime.now());
        taskRepository.save(task);
        return taskMapper.mapToTaskDTO(task);
    }





    @Transactional
    public TaskDTO updateDraftTask(Long taskId, TaskRequest taskRequest) {
        Task taskToUpdate = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        updateTaskEntityFromTaskRequest(taskRequest, taskToUpdate);
        taskRepository.save(taskToUpdate);
        return taskMapper.mapToTaskDTO(taskToUpdate);

    }

    private void updateTaskEntityFromTaskRequest(TaskRequest taskRequest, Task task) {
        if (taskRequest.getName() != null && !taskRequest.getName().isBlank())
            task.setName(taskRequest.getName());
        if (taskRequest.getDescription() != null && !taskRequest.getDescription().isBlank())
            task.setDescription(taskRequest.getDescription());
        if ( taskRequest.getPriority() != null)
            task.setPriority(taskRequest.getPriority());
        if ( taskRequest.getResponsibleId() != null )
            task.setResponsible(membershipRepository.findByIdAndTeamId(taskRequest.getResponsibleId(), taskRequest.getTeamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Responsible not found with id " + taskRequest.getResponsibleId())));
        if (taskRequest.getDeadtime() != null && !taskRequest.getDeadtime().isBlank())
            task.setDeadtime(LocalDateTime.parse(taskRequest.getDeadtime()));
        if (taskRequest.getDuration() != null && !taskRequest.getDuration().isBlank())
            task.setDuration(LocalDateTime.parse(taskRequest.getDuration()));
    }



    @Transactional
    public String deleteTask(Long taskId) {
        Task taskToDelete = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        if (taskToDelete.getTaskStatus() == TaskStatusTypes.DRAFT) {
            taskRepository.delete(taskToDelete);
            return "Task has been deleted";
        }
        else if ( NOT_COMPLETED_STATUSES.contains(taskToDelete.getTaskStatus()) ) {
            taskToDelete.setTaskStatus(TaskStatusTypes.DELETED);
            taskRepository.save(taskToDelete);
            List<TaskWorkTime> workTimes = taskWorkTimeRepository.findAllByTaskId(taskId);
            for ( TaskWorkTime workTime : workTimes )
                taskWorkTimeRepository.delete(workTime);

            return "Task has been deleted";
        }
        else {
            return "You can't delete this task";
        }
    }



    @Transactional
    public String cancelTask(Long taskId) {
        Task taskToCancel = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        if (taskToCancel.getTaskStatus() == TaskStatusTypes.DRAFT) {
            taskRepository.delete(taskToCancel);
            return "Task has been cancelled";
        } else if ( NOT_COMPLETED_STATUSES.contains(taskToCancel.getTaskStatus()) ) {
            taskToCancel.setTaskStatus(TaskStatusTypes.CANCELED);
            taskRepository.save(taskToCancel);
            List<TaskWorkTime> workTimes = taskWorkTimeRepository.findAllByTaskId(taskId);
            for ( TaskWorkTime workTime : workTimes )
                taskWorkTimeRepository.delete(workTime);

            return "Task has been cancelled";
        } else {
            return "You can't cancel this task";
        }
    }




    public List<TaskDTO> getAllTasks(User user) {
        List<Task> tasks = taskRepository.findAllByResponsibleEmployee(user);
        List<Task> filteredTasks = tasks.stream()
                .filter(task -> NOT_COMPLETED_STATUSES.contains(task.getTaskStatus()))
                .sorted(Comparator.comparing(Task::getStartTime))
                .toList();
        return filteredTasks.stream().map(taskMapper::mapToTaskDTO).collect(Collectors.toList());
    }





    public List<TaskDTO> getAllTasksHistory(User user) {
        List<Task> tasks = taskRepository.findAllByResponsibleEmployee(user);
        return tasks
                .stream()
                .filter(task -> task.getTaskStatus() != TaskStatusTypes.DRAFT)
                .sorted(Comparator.comparing(Task::getStartTime))
                .map(taskMapper::mapToTaskDTO)
                .collect(Collectors.toList());
    }




    @Transactional
    public String assignDraftTasks(User user) {
        List<Task> tasks = taskRepository.findAllByCreatedByEmployee(user);
        List<Task> draftTasks = tasks.stream().filter(task -> task.getTaskStatus() == TaskStatusTypes.DRAFT).toList();
        for (Task task : draftTasks) {
            task.setTaskStatus(TaskStatusTypes.NOT_STARTED);
            taskRepository.save(task);
        }
        return "Tasks assigned successfully";
    }





    public List<TaskDTO> getDraftTasks(User user) {
        List<Task> tasks = taskRepository.findAllByCreatedByEmployee(user);
        return tasks
                .stream()
                .filter(task -> task.getTaskStatus() == TaskStatusTypes.DRAFT)
                .sorted(Comparator.comparing(Task::getStartTime).reversed())
                .map(taskMapper::mapToTaskDTO)
                .collect(Collectors.toList());
    }




    @Transactional
    public Map<String, Object> completeTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        if (task.getTaskStatus() == TaskStatusTypes.COMPLETED)
            return Map.of("message", "Task is already completed");
        else if (NOT_COMPLETED_STATUSES.contains(task.getTaskStatus()) ) {
            if ( taskWorkTimeRepository.existsByTaskIdAndEndTimeIsNull(taskId) ) {
                TaskWorkTime taskWorkTime = taskWorkTimeRepository.findByTaskIdAndEndTimeIsNull(taskId).orElseThrow(
                        () ->  new ResourceNotFoundException("Work Time with id %d not found".formatted(taskId))
                );
                taskWorkTime.setEndTime(LocalDateTime.now());
                taskWorkTimeRepository.save(taskWorkTime);
            }
            task.setTaskStatus(TaskStatusTypes.COMPLETED);
            task.setEndTime(LocalDateTime.now());
            taskRepository.save(task);
            return Map.of("message", "Task completed successfully", "data", taskMapper.mapToTaskDTO(task));
        }
        else {
            return Map.of("message", "You can't complete this task");
        }
    }





    public List<TaskDTO> sortTasksByPriority(User user) {
        List<Task> tasks = taskRepository.findAllByResponsibleEmployee(user);

        return tasks.stream()
                .filter(task -> NOT_COMPLETED_STATUSES.contains(task.getTaskStatus()))
                .sorted(Comparator.comparing(task -> {
                    if (task.getPriority() == null)
                        return Integer.MAX_VALUE;

                    return switch (task.getPriority()) {
                        case CRITICAL -> 1;
                        case HIGH -> 2;
                        case MEDIUM -> 3;
                        case LOW -> 4;
                    };
                }, Comparator.naturalOrder()))
                .map(taskMapper::mapToTaskDTO)
                .toList();
    }


    public List<TaskDTO> sortTasksByDeadTime(User user) {
        List<Task> tasks = taskRepository.findAllByResponsibleEmployee(user);
        return tasks.stream()
                .filter(task -> NOT_COMPLETED_STATUSES.contains(task.getTaskStatus()))
                .sorted(Comparator.comparing(Task::getDeadtime, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(taskMapper::mapToTaskDTO)
                .toList();
    }


    public TaskDTO getTaskById(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        if ( (!permissionService.canManageTask(user, taskId) && task.getTaskStatus() == TaskStatusTypes.DRAFT) ||
        !permissionService.isMemberOfTeam(user, task.getResponsible().getTeam().getId(), task.getResponsible().getTeam().getCompany().getId()))
            throw new ResourceNotFoundException("You can't access this task");
        return taskMapper.mapToTaskDTO(task);
    }



    @Transactional
    public Map<String, Object> updateTask(Long taskId, TaskRequestByResponsible taskRequest, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new ResourceNotFoundException("Task with id %d not found".formatted(taskId)));
        if (taskRequest == null)
            return Map.of("message", "Task request is null");
        if ( taskRequest.getDescription() != null && !taskRequest.getDescription().isBlank() )
            task.setDescription(taskRequest.getDescription());
        if ( taskRequest.getTaskCategoryId() != null &&
                ( taskCategoryRepository.existsByIdAndIsPrimaryTrue(taskRequest.getTaskCategoryId())
                || taskCategoryRepository.existsByIdAndCreatedBy(taskId, user)))
            task.setTaskCategory(taskCategoryRepository.findById(taskRequest.getTaskCategoryId()).get());
        taskRepository.save(task);
        return Map.of("message", "Task updated successfully", "data", taskMapper.mapToTaskDTO(task));
    }
}