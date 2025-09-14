package com.example.TaskManager.controller;

import com.example.TaskManager.dto.TaskCategoryDTO;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.service.TaskCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class TaskCategoryController {
    private final TaskCategoryService taskCategoryService;

    @PostMapping("add-primary-category")
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

    @GetMapping("search-primary-categories")
    @PreAuthorize("(hasRole('ADMIN') and hasAuthority('admin:read')) or (hasRole('MANAGER') and hasAuthority('management:read'))")
    public ResponseEntity<List<TaskCategoryDTO>> findPrimaryCategoryByTitle(@RequestParam String title) {
        List<TaskCategoryDTO> taskCategoryDTOList = taskCategoryService.findPrimaryCategoryByTitle(title);
        return new ResponseEntity<>(taskCategoryDTOList, HttpStatus.OK);
    }


    @GetMapping("search-categories")
    public ResponseEntity<List<TaskCategoryDTO>> findCategoryByTitle(@RequestParam String title, @AuthenticationPrincipal User user) {
        List<TaskCategoryDTO> taskCategoryDTOList = taskCategoryService.findCategoryByTitle(title, user);
        return new ResponseEntity<>(taskCategoryDTOList, HttpStatus.OK);
    }
}
