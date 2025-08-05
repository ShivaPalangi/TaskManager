package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskStatusTypes;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class TaskStatusTypeDTO {
    private Long id;

    @NotNull(message = "اسم نوع وضعیت تسک نمی تواند خالی باشد")
    private String name;

    @NotNull(message = "نوع وضعیت تسک نمی تواند خالی باشد")
    private TaskStatusTypes taskStatusType;

    private String description;
    private List<TaskStatusDTO> taskStatusList;
}