package com.mipt.ailanakaramchakova.repository;

import com.mipt.ailanakaramchakova.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  List<Task> findByCompleted(boolean completed);

  List<Task> findByPriority(com.mipt.ailanakaramchakova.model.Priority priority);

  List<Task> findByCompletedAndPriority(boolean completed,
    com.mipt.ailanakaramchakova.model.Priority priority);

  @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :today AND :sevenDaysLater")
  List<Task> findTasksDueInNextSevenDays(
    @Param("today") LocalDate today,
    @Param("sevenDaysLater") LocalDate sevenDaysLater
  );

  @EntityGraph(attributePaths = {"attachments"})
  List<Task> findAll();
}
