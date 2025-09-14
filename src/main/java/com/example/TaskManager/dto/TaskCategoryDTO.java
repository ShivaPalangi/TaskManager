package com.example.TaskManager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class TaskCategoryDTO {
    private Long id;
    @NotNull(message = "Title is required")
    private String title;
    private List<TaskDTO> tasks;
    private Long creatorId;
}