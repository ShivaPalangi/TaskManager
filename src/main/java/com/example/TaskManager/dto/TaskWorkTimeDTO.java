package com.example.TaskManager.dto;

import lombok.Data;

@Data
public class TaskWorkTimeDTO {
    private Long id;
    private String startTime;
    private String endTime;
    private TaskDTO task;
}