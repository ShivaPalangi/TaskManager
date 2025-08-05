package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskPriorities;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class TaskDTO {
    private Long id;

    @NotNull(message = "اسم تسک نمی تواند خالی باشد")
    private String name;

    private String description;
    private String startTime;
    private String endTime;
    private String deadtime;
    private String duration;

    @NotNull(message = "اولویت تسک نمی تواند خالی باشد")
    private TaskPriorities priority;

    @NotNull(message = "آی دی نوع دسته بندی تسک نمی تواند خالی باشد")
    private TaskCategoryDTO taskCategory;

    @NotNull(message = "آی دی انجام دهنده تسک نمی تواند خالی باشد")
    private MembershipDTO responsible;

    @NotNull(message = "آی دی تعریف کننده تسک نمی تواند خالی باشد")
    private MembershipDTO createdBy;

    @NotNull(message = "آی دی وضعیت فعلی تسک نمی تواند خالی باشد")
    private TaskStatusDTO taskActiveStatus;

    private List<TaskWorkTimeDTO> workTimes;
    private List<TaskStatusDTO> taskStatusHistory;
}