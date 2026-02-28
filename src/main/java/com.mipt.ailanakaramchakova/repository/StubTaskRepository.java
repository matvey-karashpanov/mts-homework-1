package com.mipt.ailanakaramchakova.repository;

import com.mipt.ailanakaramchakova.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stub implementation of TaskRepository with fixed data.
 * Created manually via @Bean in AppConfig.
 */
public class StubTaskRepository implements TaskRepository {
    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Stub Task", "Description", false));
        return tasks;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.of(new Task(1L, "Stub Task", "Description", false));
    }

    @Override
    public Task save(Task task) {
        return task;
    }

    @Override
    public void deleteById(Long id) {
    }
}
