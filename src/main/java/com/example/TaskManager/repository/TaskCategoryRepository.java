package com.example.TaskManager.repository;

import com.example.TaskManager.entity.TaskCategory;
import com.example.TaskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCategoryRepository extends JpaRepository<TaskCategory, Long> {

    boolean existsByTitleIgnoreCaseAndIsPrimaryTrue(String title);

    boolean existsByCreatedByAndTitleIgnoreCase(User createdBy, String title);

    List<TaskCategory> findAllByIsPrimaryTrue();

    List<TaskCategory> findAllByIsPrimaryTrueOrCreatedBy(User createdBy);

    List<TaskCategory> findAllByIsPrimaryTrueAndTitleContainingIgnoreCase(String title);

    @Query("""
            SELECT tc FROM TaskCategory tc WHERE
            LOWER(tc.title) LIKE LOWER(CONCAT('%', :title, '%')) AND
            (tc.createdBy = :user OR tc.isPrimary = true)
            """)
    List<TaskCategory> findCategoriesByTitleAndUserOrPrimary(@Param("title") String title, @Param("user") User user);

    boolean existsByIdAndIsPrimaryTrue(Long id);

    boolean existsByIdAndCreatedBy(Long id, User createdBy);

}
