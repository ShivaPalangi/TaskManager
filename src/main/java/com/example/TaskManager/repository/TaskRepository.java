package com.example.TaskManager.repository;

import com.example.TaskManager.entity.Task;
import com.example.TaskManager.entity.TaskCategory;
import com.example.TaskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByTaskCategoryAndResponsibleEmployee(TaskCategory taskCategory, User employee);
    boolean existsByIdAndResponsibleEmployee(Long id, User employee);
    List<Task> findAllByResponsibleEmployee(User employee);
    List<Task> findAllByCreatedByEmployee(User employee);
    boolean existsByIdAndCreatedByEmployee(Long id,User employee);
}
