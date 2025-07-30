package com.example.TaskManager.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "task_status")
@Data
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreatedDate
    private LocalDateTime createdDate;

    private LocalDateTime deactivatedTime;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "task_status_type", nullable = false)
    private TaskStatusType taskStatusType;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "task", nullable = false)
    private Task task;

    @OneToMany(mappedBy = "taskActiveStatus", cascade = CascadeType.ALL)
    private List<Task> tasks;
}