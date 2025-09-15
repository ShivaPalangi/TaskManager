package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TaskCategoryDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.service.TaskCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class TaskCategoryController {
    private final TaskCategoryService taskCategoryService;
    private final PermissionService permissionService;

    @PostMapping("primary-categories")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:create')) or (hasRole('MANAGER') and hasAuthority('management:create'))")
    public ResponseEntity<?> addPrimaryTaskCategory(
            @Valid @RequestBody TaskCategoryDTO taskCategoryDTO,
            @AuthenticationPrincipal User user) {

        TaskCategoryDTO newTaskCategoryDTO = taskCategoryService.addPrimaryTaskCategory(taskCategoryDTO, user);
        if  (newTaskCategoryDTO == null)
            return new ResponseEntity<>("Task category is already exist",HttpStatus.OK);
        return new ResponseEntity<>(newTaskCategoryDTO, HttpStatus.CREATED);
    }



    @PostMapping
    public ResponseEntity<?> addTaskCategory(
            @Valid @RequestBody TaskCategoryDTO taskCategoryDTO,
            @AuthenticationPrincipal User user) {
        TaskCategoryDTO newTaskCategoryDTO = taskCategoryService.addTaskCategory(taskCategoryDTO, user);
        if  (newTaskCategoryDTO == null)
            return new ResponseEntity<>("Task category is already exist",HttpStatus.OK);
        return new ResponseEntity<>(newTaskCategoryDTO, HttpStatus.CREATED);
    }




    @GetMapping("primary-categories")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:read')) or (hasRole('MANAGER') and hasAuthority('management:read'))")
    public ResponseEntity<List<TaskCategoryDTO>> getAllPrimaryTaskCategories() {
        List<TaskCategoryDTO> categoryDTOList = taskCategoryService.getAllPrimaryTaskCategories();
        return new ResponseEntity<>(categoryDTOList, HttpStatus.OK);
    }



    @GetMapping
    public ResponseEntity<List<TaskCategoryDTO>> getAllUserTaskCategories(@AuthenticationPrincipal User user) {
        List<TaskCategoryDTO> categoryDTOList = taskCategoryService.getAllUserTaskCategories(user);
        return new ResponseEntity<>(categoryDTOList, HttpStatus.OK);
    }



    @GetMapping("primary-categories/search")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:read')) or (hasRole('MANAGER') and hasAuthority('management:read'))")
    public ResponseEntity<List<TaskCategoryDTO>> findPrimaryCategoryByTitle(@RequestParam String title) {
        List<TaskCategoryDTO> taskCategoryDTOList = taskCategoryService.findPrimaryCategoryByTitle(title);
        return new ResponseEntity<>(taskCategoryDTOList, HttpStatus.OK);
    }



    @GetMapping("search-categories")
    public ResponseEntity<List<TaskCategoryDTO>> findCategoryByTitle(
            @RequestParam String title,
            @AuthenticationPrincipal User user) {

        List<TaskCategoryDTO> taskCategoryDTOList = taskCategoryService.findCategoryByTitle(title, user);
        return new ResponseEntity<>(taskCategoryDTOList, HttpStatus.OK);
    }



    @GetMapping("{id}")
    public ResponseEntity<TaskCategoryDTO> getTaskCategoryById(
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canGetTaskCategoryDetail(user, id))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        TaskCategoryDTO categoryDTO = taskCategoryService.getTaskCategoryById(id, user);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }



    @GetMapping("primary-categories/{id}")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:read')) or (hasRole('MANAGER') and hasAuthority('management:read'))")
    public ResponseEntity<TaskCategoryDTO> getPrimaryTaskCategoryById(@PathVariable @Min(1) Long id) throws AccessDeniedException {

        if ( !permissionService.canManagePrimaryTaskDetail(id))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        TaskCategoryDTO categoryDTO = taskCategoryService.getPrimaryTaskCategoryById(id);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }



    @PatchMapping("primary-categories/{id}")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:update')) or (hasRole('MANAGER') and hasAuthority('management:update'))")
    public ResponseEntity<TaskCategoryDTO> updatePrimaryTaskCategory(
            @Min(1) @PathVariable Long id,
            @Valid @RequestBody TaskCategoryDTO taskCategoryDTO) throws AccessDeniedException {

        if ( !permissionService.canManagePrimaryTaskDetail(id))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        TaskCategoryDTO categoryDTO = taskCategoryService.updatePrimaryTaskCategory(id, taskCategoryDTO);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }



    @DeleteMapping("primary-categories/{id}")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:delete')) or (hasRole('MANAGER') and hasAuthority('management:delete'))")
    public ResponseEntity<String> deletePrimaryTaskCategory(@Min(1) @PathVariable Long id) throws AccessDeniedException {

        if ( !permissionService.canManagePrimaryTaskDetail(id))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        String response = taskCategoryService.deleteTaskCategory(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PatchMapping("{id}")
    public ResponseEntity<TaskCategoryDTO> updateTaskCategory(
            @Min(1) @PathVariable Long id,
            @Valid @RequestBody TaskCategoryDTO taskCategoryDTO,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTaskCategory(id, user))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        TaskCategoryDTO categoryDTO = taskCategoryService.updateTaskCategory(id, taskCategoryDTO);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }



    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTaskCategory(
            @Min(1) @PathVariable Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTaskCategory(id, user))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        String response = taskCategoryService.deleteTaskCategory(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
