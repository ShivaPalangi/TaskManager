package com.example.TaskManager.entity;

import com.example.TaskManager.enums.TaskPriorities;
import com.example.TaskManager.enums.TaskStatusTypes;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "task")
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime deadtime;
    private LocalDateTime duration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriorities priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatusTypes taskStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_category", nullable = false)
    private TaskCategory taskCategory;

    @ManyToOne(optional = false)
    @JoinColumn(name = "responsible", nullable = false)
    private Membership responsible;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private Membership createdBy;

    @OneToMany(mappedBy = "task")
    private List<TaskWorkTime> workTimes;
}