package com.mipt.ailanakaramchakova.repository;

import com.mipt.ailanakaramchakova.model.Task;
import java.util.List;
import java.util.Optional;

/**
 * Interface for task CRUD operations.
 */
public interface TaskRepository {

  List<Task> findAll();

  Optional<Task> findById(Long id);

  Task save(Task task);

  void deleteById(Long id);
}
