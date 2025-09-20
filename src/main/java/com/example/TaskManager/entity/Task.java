package com.example.TaskManager.entity;

import com.example.TaskManager.enums.TaskPriorities;
import com.example.TaskManager.enums.TaskStatusTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "task_category")
    private TaskCategory taskCategory;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "responsible")
    private Membership responsible;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "created_by")
    private Membership createdBy;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<TaskWorkTime> workTimes;
}