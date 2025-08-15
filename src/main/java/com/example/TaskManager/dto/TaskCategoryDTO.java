package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskCategories;
import lombok.Data;
import java.util.List;

@Data
public class TaskCategoryDTO {
    private Long id;
    private TaskCategories category;
    private List<TaskDTO> tasks;
}