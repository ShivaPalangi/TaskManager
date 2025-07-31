package com.example.TaskManager.dto;

import com.example.TaskManager.enums.TaskPriorities;
import lombok.Data;
import java.util.List;

@Data
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String deadtime;
    private String duration;
    private TaskPriorities priority;
    private TaskCategoryDTO taskCategory;
    private MembershipDTO responsible;
    private MembershipDTO createdBy;
    private List<TaskWorkTimeDTO> workTimes;
    private List<TaskStatusDTO> taskStatusHistory;
    private TaskStatusDTO taskActiveStatus;
}
