package com.example.TaskManager.repository;

import com.example.TaskManager.entity.TaskStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusTypeRepository extends JpaRepository<TaskStatusType, Long> {
}
