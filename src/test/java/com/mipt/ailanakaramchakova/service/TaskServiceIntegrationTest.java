package com.mipt.ailanakaramchakova.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mipt.ailanakaramchakova.exception.TaskNotFoundException;
import com.mipt.ailanakaramchakova.model.Priority;
import com.mipt.ailanakaramchakova.model.Task;
import com.mipt.ailanakaramchakova.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

/**
 * Integration tests for TaskService with transaction rollback.
 */
@SpringBootTest
@ActiveProfiles("test")
public class TaskServiceIntegrationTest {

  @Autowired
  private TaskService taskService;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  public void testBulkCompleteTasks_Success() {
    Task task1 = new Task(null, "Task 1", "Desc", false);
    task1.setPriority(Priority.MEDIUM);
    Task task2 = new Task(null, "Task 2", "Desc", false);
    task2.setPriority(Priority.MEDIUM);

    task1 = taskRepository.save(task1);
    task2 = taskRepository.save(task2);

    taskService.bulkCompleteTasks(Arrays.asList(task1.getId(), task2.getId()));

    Task updated1 = taskRepository.findById(task1.getId()).orElseThrow();
    Task updated2 = taskRepository.findById(task2.getId()).orElseThrow();

    assertTrue(updated1.isCompleted());
    assertTrue(updated2.isCompleted());
  }

  @Test
  @Transactional
  public void testBulkCompleteTasks_RollbackOnNotFound() {
    Task task1 = new Task(null, "Task 1", "Desc", false);
    task1.setPriority(Priority.MEDIUM);

    Task savedTask1 = taskRepository.save(task1);

    Long invalidId = 999L;

    assertThrows(TaskNotFoundException.class, () -> {
      taskService.bulkCompleteTasks(Arrays.asList(savedTask1.getId(), invalidId));
    });

    Task notUpdated = taskRepository.findById(savedTask1.getId()).orElseThrow();
    assertFalse(notUpdated.isCompleted());
  }
}
