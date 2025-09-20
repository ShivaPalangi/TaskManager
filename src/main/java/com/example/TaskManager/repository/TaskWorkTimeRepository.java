package com.example.TaskManager.repository;

import com.example.TaskManager.entity.TaskWorkTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskWorkTimeRepository extends JpaRepository<TaskWorkTime, Long> {
    boolean existsByTaskIdAndEndTimeIsNull(Long taskId);
    Optional<TaskWorkTime> findByTaskIdAndEndTimeIsNull(Long taskId);
    List<TaskWorkTime> findAllByTaskId(Long taskId);
}
