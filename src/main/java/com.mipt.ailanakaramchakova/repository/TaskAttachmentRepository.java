package com.mipt.ailanakaramchakova.repository;

import com.mipt.ailanakaramchakova.model.TaskAttachment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

/**
 * Repository for task attachments.
 */
@Repository
public class TaskAttachmentRepository {

  private final Map<Long, TaskAttachment> storage = new ConcurrentHashMap<>();
  private final AtomicLong currentId = new AtomicLong(1L);

  public TaskAttachment save(TaskAttachment attachment) {
    if (attachment.getId() == null) {
      attachment.setId(currentId.getAndIncrement());
    }
    storage.put(attachment.getId(), attachment);
    return attachment;
  }

  public Optional<TaskAttachment> findById(Long id) {
    return Optional.ofNullable(storage.get(id));
  }

  public List<TaskAttachment> findByTaskId(Long taskId) {
    List<TaskAttachment> result = new ArrayList<>();
    for (TaskAttachment attachment : storage.values()) {
      if (attachment.getTaskId().equals(taskId)) {
        result.add(attachment);
      }
    }
    return result;
  }

  public boolean deleteById(Long id) {
    return storage.remove(id) != null;
  }
}
