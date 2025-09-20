package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TaskCategoryDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.PermissionService;
import com.example.TaskManager.service.TaskCategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class TaskCategoryController {
    private final TaskCategoryService taskCategoryService;
    private final PermissionService permissionService;

    @PostMapping("/primary-categories")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:create')) or (hasRole('MANAGER') and hasAuthority('management:create'))")
    public ResponseEntity<?> addPrimaryTaskCategory(
            @Valid @RequestBody TaskCategoryDTO taskCategoryDTO,
            @AuthenticationPrincipal User user) {

        TaskCategoryDTO newCategory = taskCategoryService.addPrimaryTaskCategory(taskCategoryDTO, user);
        if  (newCategory == null)
            return ResponseEntity.ok("Task category is already exist");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCategory.getId())
                .toUri();

        return ResponseEntity.created(location).body(
                Map.of("message", "Task category created successfully", "data",  newCategory));
    }



    @PostMapping
    public ResponseEntity<?> addTaskCategory(
            @Valid @RequestBody TaskCategoryDTO taskCategoryDTO,
            @AuthenticationPrincipal User user) {
        TaskCategoryDTO newCategory = taskCategoryService.addTaskCategory(taskCategoryDTO, user);
        if  (newCategory == null)
            return ResponseEntity.ok("Task category is already exist");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCategory.getId())
                .toUri();
        return ResponseEntity.created(location).body(
                Map.of("message", "Task category created successfully", "data",  newCategory));
    }




    @GetMapping("/primary-categories")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:read')) or (hasRole('MANAGER') and hasAuthority('management:read'))")
    public ResponseEntity<List<TaskCategoryDTO>> getAllPrimaryTaskCategories() {
        List<TaskCategoryDTO> categoryDTOList = taskCategoryService.getAllPrimaryTaskCategories();
        return ResponseEntity.ok(categoryDTOList);
    }



    @GetMapping
    public ResponseEntity<List<TaskCategoryDTO>> getAllUserTaskCategories(@AuthenticationPrincipal User user) {
        List<TaskCategoryDTO> categoryDTOList = taskCategoryService.getAllUserTaskCategories(user);
        return ResponseEntity.ok(categoryDTOList);
    }



    @GetMapping("/primary-categories/search")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:read')) or (hasRole('MANAGER') and hasAuthority('management:read'))")
    public ResponseEntity<List<TaskCategoryDTO>> findPrimaryCategoryByTitle(@RequestParam String title) {
        List<TaskCategoryDTO> taskCategoryDTOList = taskCategoryService.findPrimaryCategoryByTitle(title);
        return ResponseEntity.ok(taskCategoryDTOList);
    }



    @GetMapping("/search")
    public ResponseEntity<List<TaskCategoryDTO>> findCategoryByTitle(
            @RequestParam String title,
            @AuthenticationPrincipal User user) {

        List<TaskCategoryDTO> taskCategoryDTOList = taskCategoryService.findCategoryByTitle(title, user);
        return ResponseEntity.ok(taskCategoryDTOList);
    }



    @GetMapping("/{id}")
    public ResponseEntity<TaskCategoryDTO> getTaskCategoryById(
            @PathVariable @Min(1) Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canGetTaskCategoryDetail(user, id))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        TaskCategoryDTO categoryDTO = taskCategoryService.getTaskCategoryById(id, user);
        return ResponseEntity.ok(categoryDTO);
    }



    @GetMapping("/primary-categories/{id}")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:read')) or (hasRole('MANAGER') and hasAuthority('management:read'))")
    public ResponseEntity<TaskCategoryDTO> getPrimaryTaskCategoryById(@PathVariable @Min(1) Long id) throws AccessDeniedException {

        if ( !permissionService.canManagePrimaryTaskDetail(id))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        TaskCategoryDTO categoryDTO = taskCategoryService.getPrimaryTaskCategoryById(id);
        return ResponseEntity.ok(categoryDTO);
    }



    @PatchMapping("/primary-categories/{id}")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:update')) or (hasRole('MANAGER') and hasAuthority('management:update'))")
    public ResponseEntity<?> updatePrimaryTaskCategory(
            @Min(1) @PathVariable Long id,
            @Valid @RequestBody TaskCategoryDTO taskCategoryDTO) throws AccessDeniedException {

        if ( !permissionService.canManagePrimaryTaskDetail(id))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        TaskCategoryDTO categoryDTO = taskCategoryService.updatePrimaryTaskCategory(id, taskCategoryDTO);
        return ResponseEntity.ok(
                Map.of("message", "Task category updated successfully", "data", categoryDTO));
    }



    @DeleteMapping("/primary-categories/{id}")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:delete')) or (hasRole('MANAGER') and hasAuthority('management:delete'))")
    public ResponseEntity<?> deletePrimaryTaskCategory(@Min(1) @PathVariable Long id) throws AccessDeniedException {

        if ( !permissionService.canManagePrimaryTaskDetail(id))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        taskCategoryService.deleteTaskCategory(id);

        String redirectUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("api/v1/categories/primary-categories")
                .build()
                .toUriString();

        Map<String, Object> response = Map.of(
                "redirect", true,
                "redirectUrl", redirectUrl,
                "message", "Task category with id %d deleted successfully".formatted(id)
        );
        return ResponseEntity.ok(response);
    }



    @PatchMapping("/{id}")
    public ResponseEntity<?> updateTaskCategory(
            @Min(1) @PathVariable Long id,
            @Valid @RequestBody TaskCategoryDTO taskCategoryDTO,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTaskCategory(id, user))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        TaskCategoryDTO categoryDTO = taskCategoryService.updateTaskCategory(id, taskCategoryDTO, user);
        return ResponseEntity.ok(
                Map.of("message", "Task category updated successfully", "data", categoryDTO));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaskCategory(
            @Min(1) @PathVariable Long id,
            @AuthenticationPrincipal User user) throws AccessDeniedException {

        if ( !permissionService.canManageTaskCategory(id, user))
            throw new AccessDeniedException("Task category with id %d not found!".formatted(id));

        taskCategoryService.deleteTaskCategory(id);

        String redirectUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("api/v1/categories")
                .build()
                .toUriString();

        Map<String, Object> response = Map.of(
                "redirect", true,
                "redirectUrl", redirectUrl,
                "message", "Task category with id %d deleted successfully".formatted(id)
        );
        return ResponseEntity.ok(response);
    }
}
