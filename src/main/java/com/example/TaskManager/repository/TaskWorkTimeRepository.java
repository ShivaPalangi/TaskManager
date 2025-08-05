package com.example.TaskManager.repository;

import com.example.TaskManager.entity.TaskWorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskWorkTimeRepository extends JpaRepository<TaskWorkTime, Long> {
}
