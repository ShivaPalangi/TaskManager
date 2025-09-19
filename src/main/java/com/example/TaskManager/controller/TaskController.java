package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.service.TaskService;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final PermissionService permissionService;

    @PostMapping("add-task")
    public ResponseEntity<?> addTask(
            @Validated(ValidationGroups.Create.class) @RequestBody TaskDTO taskDTO,
            @RequestParam Long teamId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTeam(user, teamId))
            throw new AccessDeniedException("You can't assign task to any membership");
        var response = taskService.addTask(taskDTO, teamId, user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PatchMapping("{id}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody TaskDTO taskDTO,
            @AuthenticationPrincipal User user
    ) throws AccessDeniedException {

        if ( !permissionService.canManageTask(user, id))
            throw new AccessDeniedException("You are not allowed to update this task");
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }



    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTask(user, id))
            throw new AccessDeniedException("You are not allowed to delete this task");
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task has been deleted", HttpStatus.OK);
    }




    @PatchMapping("{id}/cancel")
    public ResponseEntity<String> cancelTask(
            @PathVariable Long taskId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTask(user, taskId))
            throw new AccessDeniedException("You are not allowed to delete this task");
        taskService.cancelTask(taskId);
        return new ResponseEntity<>("Task has been canceled", HttpStatus.OK);
    }



    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@AuthenticationPrincipal User user) {
        List<TaskDTO> tasks = taskService.getAllTasks(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("tasks-history")
    public ResponseEntity<List<TaskDTO>> getAllTasksHistory(@AuthenticationPrincipal User user) {
        List<TaskDTO> taskDTOS = taskService.getAllTasksHistory(user);
        return new ResponseEntity<>(taskDTOS, HttpStatus.OK);
    }


    @PatchMapping("assign-tasks")
    public ResponseEntity<List<TaskDTO>> assignTasks(@AuthenticationPrincipal User user) {
        List<TaskDTO> assignedTasks = taskService.assignTasks(user);
        return new ResponseEntity<>(assignedTasks, HttpStatus.OK);
    }


    @GetMapping("draft_tasks")
    public ResponseEntity<List<TaskDTO>> getDraftTasks(@AuthenticationPrincipal User user) {
        List<TaskDTO> draftTasks = taskService.getDraftTasks(user);
        return new ResponseEntity<>(draftTasks, HttpStatus.OK);
    }


    @PatchMapping("{id}/complete")
    public ResponseEntity<TaskDTO> completeTask(@AuthenticationPrincipal User user, @PathVariable Long taskId) throws AccessDeniedException {
        if ( !permissionService.isResponsible(taskId, user))
            throw new AccessDeniedException("You are not the responsible of this task");
        TaskDTO completedTask = taskService.completeTask(taskId, user);
        return new ResponseEntity<>(completedTask, HttpStatus.OK);
    }


    @GetMapping("sort-by-priority")
    public ResponseEntity<List<TaskDTO>> sortTasksByPriority(@AuthenticationPrincipal User user) {
        List<TaskDTO> tasks = taskService.sortTasksByPriority(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }


    @GetMapping("sort-by-dead-time")
    public ResponseEntity<List<TaskDTO>> sortTasksByDeadTime(@AuthenticationPrincipal User user) {
        List<TaskDTO> tasks = taskService.sortTasksByDeadTime(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }



    @GetMapping("{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@AuthenticationPrincipal User user, @PathVariable Long taskId) {
        TaskDTO taskDTO = taskService.getTaskById(taskId, user);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }


}
