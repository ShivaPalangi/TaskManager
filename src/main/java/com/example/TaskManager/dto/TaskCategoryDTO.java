package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskCategories;
import com.example.TaskManager.validation.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class TaskCategoryDTO {
    private Long id;
    @NotNull(message = "Category is required", groups = ValidationGroups.Create.class)
    private TaskCategories category;
    private List<TaskDTO> tasks;
}