package com.mipt.ailanakaramchakova.repository;

import com.mipt.ailanakaramchakova.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Primary in-memory implementation of TaskRepository.
 */
@Primary
@Repository
public class InMemoryTaskRepository implements TaskRepository {

  private final Map<Long, Task> storage = new ConcurrentHashMap<>();
  private final AtomicLong currentId = new AtomicLong(1L);

  @Override
  public List<Task> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public Optional<Task> findById(Long id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Task save(Task task) {
    if (task.getId() == null) {
      task.setId(currentId.getAndIncrement());
      task.setCreatedAt(LocalDateTime.now());
    }
    storage.put(task.getId(), task);
    return task;
  }

  @Override
  public boolean deleteById(Long id) {
    return storage.remove(id) != null;
  }
}
