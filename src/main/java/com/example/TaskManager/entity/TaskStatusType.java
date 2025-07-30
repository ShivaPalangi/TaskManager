package com.example.TaskManager.entity;

import com.example.TaskManager.enums.TaskStatusTypes;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "task_status_type")
@Data
public class TaskStatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatusTypes taskStatusType;

    @OneToMany(mappedBy = "taskStatusType", cascade = CascadeType.ALL)
    private List<TaskStatus> taskStatusList;
}