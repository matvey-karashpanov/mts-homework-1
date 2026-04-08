package com.mipt.ailanakaramchakova.repository;

import com.mipt.ailanakaramchakova.model.TaskAttachment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for task attachment CRUD operations with JPA.
 */
@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {

  List<TaskAttachment> findByTaskId(Long taskId);

  boolean existsByTaskId(Long taskId);
}
