package com.mipt.ailanakaramchakova.repository;

import com.mipt.ailanakaramchakova.model.Task;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stub implementation of TaskRepository with fixed data.
 */
public class StubTaskRepository implements TaskRepository {

  @Override
  public List<Task> findAll() {
    List<Task> tasks = new ArrayList<>();
    Task task = new Task(1L, "Stub Task", "Description", false, LocalDateTime.now(), null, null,
      null);
    tasks.add(task);
    return tasks;
  }

  @Override
  public Optional<Task> findById(Long id) {
    Task task = new Task(1L, "Stub Task", "Description", false, LocalDateTime.now(), null, null,
      null);
    return Optional.of(task);
  }

  @Override
  public Task save(Task task) {
    return task;
  }

  @Override
  public boolean deleteById(Long id) {
    return true;
  }
}
