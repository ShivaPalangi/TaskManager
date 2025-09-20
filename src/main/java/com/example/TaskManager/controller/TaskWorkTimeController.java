package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TaskWorkTimeDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.service.TaskWorkTimeService;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("api/v1/tasks/{taskId}/work-times")
@RequiredArgsConstructor
public class TaskWorkTimeController {
    private final TaskWorkTimeService taskWorkTimeService;
    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<?> startWorkTime(
            @PathVariable("taskId") @Min(1) Long taskId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.isResponsible(taskId ,user))
            throw new AccessDeniedException("You are not responsible of this task");
        Map<String, Object> response = taskWorkTimeService.startWorkTime(taskId);

        if (response.containsKey("data")) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/{workTimeId}")
                    .buildAndExpand( ((TaskWorkTimeDTO) response.get("data")).getId())
                    .toUri();

            return ResponseEntity.created(location).body(response);}
        else
            return ResponseEntity.ok(response);
    }



    @PatchMapping("/end")
    public ResponseEntity<?> endTaskWork(
            @PathVariable("taskId") Long taskId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.isResponsible(taskId ,user))
            throw new AccessDeniedException("You are not responsible of this task");

        TaskWorkTimeDTO taskWorkTime = taskWorkTimeService.endActiveTaskWork(taskId);

        String redirectUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        Map<String, Object> response = Map.of(
                "redirect", true,
                "redirectUrl", redirectUrl,
                "message", "Work time has been ended successfully",
                "data", taskWorkTime
        );
        return ResponseEntity.ok(response);
    }



    @GetMapping
    public ResponseEntity<List<TaskWorkTimeDTO>> getTaskWorkHistory(
            @PathVariable("taskId") Long taskId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {
        if ( !permissionService.canGetTaskDetail(user, taskId))
            throw new AccessDeniedException("You are not allowed to access this task");

        List<TaskWorkTimeDTO> workHistory = taskWorkTimeService.getTaskWorkHistory(taskId);
        return ResponseEntity.ok(workHistory);
    }


    @GetMapping("/active")
    public ResponseEntity<TaskWorkTimeDTO> getActiveWorkSession(
            @PathVariable("taskId") Long taskId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canGetTaskDetail(user, taskId))
            throw new AccessDeniedException("You are not allowed to access this task");

        TaskWorkTimeDTO taskWorkTime = taskWorkTimeService.getActiveWorkSession(taskId);
        return ResponseEntity.ok(taskWorkTime);
    }


}


