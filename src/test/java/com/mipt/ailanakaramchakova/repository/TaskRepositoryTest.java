package com.mipt.ailanakaramchakova.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mipt.ailanakaramchakova.model.Priority;
import com.mipt.ailanakaramchakova.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * Tests for TaskRepository with H2 database.
 */
@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryTest {

  @Autowired
  private TaskRepository taskRepository;

  @Test
  public void testSaveAndFind() {
    Task task = new Task(null, "Test Task", "Description", false);
    task.setPriority(Priority.HIGH);
    task.setDueDate(LocalDate.now().plusDays(7));

    Task saved = taskRepository.save(task);

    assertNotNull(saved.getId());
    Optional<Task> found = taskRepository.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals("Test Task", found.get().getTitle());
  }

  @Test
  public void testFindByCompleted() {
    Task task1 = new Task(null, "Task 1", "Desc", true);
    task1.setPriority(Priority.MEDIUM);
    Task task2 = new Task(null, "Task 2", "Desc", false);
    task2.setPriority(Priority.MEDIUM);

    taskRepository.save(task1);
    taskRepository.save(task2);

    List<Task> completed = taskRepository.findByCompleted(true);
    assertEquals(1, completed.size());
    assertTrue(completed.get(0).isCompleted());
  }

  @Test
  public void testFindByPriority() {
    Task task = new Task(null, "Task", "Desc", false);
    task.setPriority(Priority.HIGH);
    taskRepository.save(task);

    List<Task> highPriority = taskRepository.findByPriority(Priority.HIGH);
    assertEquals(1, highPriority.size());
    assertEquals(Priority.HIGH, highPriority.get(0).getPriority());
  }

  @Test
  public void testFindTasksDueInNextSevenDays() {
    Task task1 = new Task(null, "Task 1", "Desc", false);
    task1.setPriority(Priority.MEDIUM);
    task1.setDueDate(LocalDate.now().plusDays(3));

    Task task2 = new Task(null, "Task 2", "Desc", false);
    task2.setPriority(Priority.MEDIUM);
    task2.setDueDate(LocalDate.now().plusDays(30));

    taskRepository.save(task1);
    taskRepository.save(task2);

    List<Task> dueSoon = taskRepository.findTasksDueInNextSevenDays(
      LocalDate.now(),
      LocalDate.now().plusDays(7)
    );

    assertEquals(1, dueSoon.size());
    assertEquals("Task 1", dueSoon.get(0).getTitle());
  }

  @Test
  public void testDelete() {
    Task task = new Task(null, "Task", "Desc", false);
    task.setPriority(Priority.MEDIUM);
    Task saved = taskRepository.save(task);

    taskRepository.deleteById(saved.getId());

    Optional<Task> found = taskRepository.findById(saved.getId());
    assertFalse(found.isPresent());
  }
}
