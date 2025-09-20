package com.example.TaskManager.dto;

import lombok.Data;

@Data
public class TaskRequestByResponsible {
    private String description;
    private Long taskCategoryId;
}
