package com.example.TaskManager.repository;

import com.example.TaskManager.entity.TaskCategory;
import com.example.TaskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {

    boolean existsByTitleIgnoreCaseAndIsPrimaryTrue(String title);

    boolean existsByCreatedByAndTitleIgnoreCase(User createdBy, String title);

    List<TaskCategory> findAllByIsPrimaryTrue();

    List<TaskCategory> findAllByIsPrimaryTrueOrCreatedBy(User createdBy);

    List<TaskCategory> findAllByIsPrimaryTrueAndTitleContainingIgnoreCase(String title);

    boolean existsByIdAndIsPrimaryTrue(Long id);

    boolean existsByIdAndCreatedBy(Long id, User createdBy);
}
