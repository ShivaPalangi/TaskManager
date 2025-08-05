package com.example.TaskManager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class TaskStatusDTO {
    private Long id;

    @NotNull(message = "زمان شروع نمی تواند خالی باشد")
    private String createdDate;

    private String deactivatedTime;

    @NotNull(message = "آی دی نوع وضعیت تسک نمی تواند خالی باشد")
    private TaskStatusTypeDTO taskStatusType;

    @NotNull(message = "آی دی تسک نمی تواند خالی باشد")
    private TaskDTO task;

    private List<TaskDTO> tasks;
}