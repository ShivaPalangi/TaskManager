package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TaskWorkTimeDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.service.TaskWorkTimeService;
import com.example.TaskManager.validation.ValidationGroups;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("api/v1/task-work-time")
@RequiredArgsConstructor
public class TaskWorkTimeController {
    private final TaskWorkTimeService taskWorkTimeService;
    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<TaskWorkTimeDTO> startWorkTime(
            @RequestBody @Validated(ValidationGroups.Create.class) TaskWorkTimeDTO taskWorkTimeDTO,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.isResponsible(taskWorkTimeDTO.getTaskId() ,user))
            throw new AccessDeniedException("You are not responsible of this task");
        TaskWorkTimeDTO workTime = taskWorkTimeService.startWorkTime(taskWorkTimeDTO);
        return new ResponseEntity<>(workTime, HttpStatus.CREATED);
    }



    @PatchMapping("/{TaskId}/end")
    public ResponseEntity<?> endTaskWork(
            @PathVariable("TaskId") Long taskId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.isResponsible(taskId ,user))
            throw new AccessDeniedException("You are not responsible of this task");

        TaskWorkTimeDTO taskWorkTime = taskWorkTimeService.endActiveTaskWork(taskId);
        return new ResponseEntity<>(taskWorkTime, HttpStatus.OK);
    }



    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskWorkTimeDTO>> getTaskWorkHistory(
            @PathVariable("TaskId") Long taskId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {
        if ( !permissionService.canGetTaskDetail(user, taskId))
            throw new AccessDeniedException("You are not allowed to access this task");

        List<TaskWorkTimeDTO> workHistory = taskWorkTimeService.getTaskWorkHistory(taskId);
        return new ResponseEntity<>(workHistory, HttpStatus.OK);
    }


    @GetMapping("/task/{taskId}/active")
    public ResponseEntity<TaskWorkTimeDTO> getActiveWorkSession(
            @PathVariable("TaskId") Long taskId,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canGetTaskDetail(user, taskId))
            throw new AccessDeniedException("You are not allowed to access this task");

        TaskWorkTimeDTO taskWorkTime = taskWorkTimeService.getActiveWorkSession(taskId);
        return new ResponseEntity<>(taskWorkTime, HttpStatus.OK);
    }


}


