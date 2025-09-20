package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.dto.TaskRequest;
import com.example.TaskManager.dto.TaskRequestByResponsible;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.service.TaskService;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final PermissionService permissionService;

    @PostMapping("/draft_tasks")
    public ResponseEntity<?> addTask(
            @Validated(ValidationGroups.Create.class) @RequestBody TaskRequest taskRequest,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTeam(user, taskRequest.getTeamId()))
            throw new AccessDeniedException("You can't assign task to any membership");

        TaskDTO response = taskService.addTask(taskRequest, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/draft_tasks")
                .build()
                .toUri();

        return ResponseEntity.created(location).body(
                Map.of("message", "Task added successfully!", "data", response));
    }



    @PatchMapping("/draft_tasks/{id}")
    public ResponseEntity<?> updateDraftTask(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody TaskRequest taskRequest,
            @AuthenticationPrincipal User user
    ) throws AccessDeniedException {

        if ( !permissionService.canManageTask(user, id))
            throw new AccessDeniedException("You are not allowed to update this task");

        TaskDTO updatedTask = taskService.updateDraftTask(id, taskRequest);
        return ResponseEntity.ok(
                Map.of("message", "Task added successfully!", "data", updatedTask));
    }




    @PatchMapping("{taskId}")
    public ResponseEntity<?> updateTask(
            @PathVariable("taskId") @Min(1) Long taskId,
            @Valid @RequestBody TaskRequestByResponsible taskRequest,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.isResponsible(taskId, user))
            throw new AccessDeniedException("You are not the responsible of this task!");

        Map<String, Object> response =  taskService.updateTask(taskId, taskRequest, user);
        return ResponseEntity.ok(response);
    }




    @PatchMapping("/assign-tasks")
    public ResponseEntity<String> assignDraftTasks(@AuthenticationPrincipal User user) {
        String response = taskService.assignDraftTasks(user);
        return ResponseEntity.ok(response);
    }





    @GetMapping("/draft_tasks")
    public ResponseEntity<List<TaskDTO>> getDraftTasks(@AuthenticationPrincipal User user) {
        List<TaskDTO> draftTasks = taskService.getDraftTasks(user);
        return ResponseEntity.ok(draftTasks);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTask(user, id))
            throw new AccessDeniedException("You are not allowed to delete this task");
        String response = taskService.deleteTask(id);
        return ResponseEntity.ok(response);
    }




    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTask(user, id))
            throw new AccessDeniedException("You are not allowed to delete this task");
        String response = taskService.cancelTask(id);
        return ResponseEntity.ok(response);
    }



    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(@AuthenticationPrincipal User user) {
        List<TaskDTO> tasks = taskService.getAllTasks(user);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks-history")
    public ResponseEntity<List<TaskDTO>> getAllTasksHistory(@AuthenticationPrincipal User user) {
        List<TaskDTO> taskDTOS = taskService.getAllTasksHistory(user);
        return ResponseEntity.ok(taskDTOS);
    }


    @PatchMapping("/{id}/complete")
    public ResponseEntity<?> completeTask(@AuthenticationPrincipal User user, @PathVariable Long id) throws AccessDeniedException {
        if ( !permissionService.isResponsible(id, user))
            throw new AccessDeniedException("You are not the responsible of this task");
        Map<String, Object> response = taskService.completeTask(id, user);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/sort-by-priority")
    public ResponseEntity<List<TaskDTO>> sortTasksByPriority(@AuthenticationPrincipal User user) {
        List<TaskDTO> tasks = taskService.sortTasksByPriority(user);
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/sort-by-dead-time")
    public ResponseEntity<List<TaskDTO>> sortTasksByDeadTime(@AuthenticationPrincipal User user) {
        List<TaskDTO> tasks = taskService.sortTasksByDeadTime(user);
        return ResponseEntity.ok(tasks);
    }



    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@AuthenticationPrincipal User user, @PathVariable Long taskId) {

        TaskDTO taskDTO = taskService.getTaskById(taskId, user);
        return  ResponseEntity.ok(taskDTO);
    }


}
