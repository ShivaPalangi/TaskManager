package com.example.TaskManager.entity;

import com.example.TaskManager.enums.TaskCategories;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "task_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskCategories category;

    @OneToMany(mappedBy = "taskCategory")
    private List<Task> tasks;
}