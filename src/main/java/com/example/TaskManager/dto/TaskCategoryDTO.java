package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskCategories;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class TaskCategoryDTO {
    private long id;

    @NotNull(message = "دسته بندی تسک نمی تواند خالی باشد")
    private TaskCategories category;

    private List<TaskDTO> tasks;
}