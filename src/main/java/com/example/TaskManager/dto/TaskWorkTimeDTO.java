package com.example.TaskManager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskWorkTimeDTO {
    private Long id;

    @NotNull(message = "زمان شروع نمی تواند خالی باشد")
    private String startTime;

    @NotNull(message = "زمان پایان نمی تواند خالی باشد")
    private String endTime;

    @NotNull(message = "آی دی تسک نمی تواند خالی باشد")
    private TaskDTO task;
}