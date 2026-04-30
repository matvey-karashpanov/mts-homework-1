package com.mipt.ailanakaramchakova.service;

import com.mipt.ailanakaramchakova.exception.TaskNotFoundException;
import com.mipt.ailanakaramchakova.model.Task;
import com.mipt.ailanakaramchakova.model.TaskAttachment;
import com.mipt.ailanakaramchakova.repository.TaskAttachmentRepository;
import com.mipt.ailanakaramchakova.repository.TaskRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for managing task attachments.
 */
@Service
public class AttachmentService {

  private final TaskAttachmentRepository repository;
  private final TaskRepository taskRepository;
  private final String uploadDir = "uploads/";

  public AttachmentService(TaskAttachmentRepository repository, TaskRepository taskRepository) {
    this.repository = repository;
    this.taskRepository = taskRepository;
    createUploadDirectory();
  }

  private void createUploadDirectory() {
    try {
      Files.createDirectories(Paths.get(uploadDir));
    } catch (IOException e) {
      throw new RuntimeException("Failed to create upload directory", e);
    }
  }

  public TaskAttachment storeAttachment(Long taskId, MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    Task task = taskRepository.findById(taskId)
      .orElseThrow(() -> new TaskNotFoundException(taskId));

    String originalFileName = file.getOriginalFilename();
    String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
    Path filePath = Paths.get(uploadDir).resolve(storedFileName);

    try {
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new IOException("Failed to store file", e);
    }

    TaskAttachment attachment = new TaskAttachment();
    attachment.setTask(task);
    attachment.setFileName(originalFileName);
    attachment.setStoredFileName(storedFileName);
    attachment.setContentType(file.getContentType());
    attachment.setSize(file.getSize());
    attachment.setUploadedAt(LocalDateTime.now());

    return repository.save(attachment);
  }

  public TaskAttachment getAttachment(Long attachmentId) {
    return repository.findById(attachmentId)
      .orElseThrow(() -> new TaskNotFoundException(attachmentId));
  }

  public Resource loadAsResource(Long attachmentId) throws IOException {
    TaskAttachment attachment = getAttachment(attachmentId);
    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Resource resource = new UrlResource(filePath.toUri());
    if (!resource.exists() || !resource.isReadable()) {
      throw new IOException("Could not read file: " + attachment.getStoredFileName());
    }
    return resource;
  }

  public void deleteAttachment(Long attachmentId) throws IOException {
    TaskAttachment attachment = getAttachment(attachmentId);
    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Files.deleteIfExists(filePath);
    repository.deleteById(attachmentId);
  }

  public List<TaskAttachment> getAttachmentsByTaskId(Long taskId) {
    return repository.findByTaskId(taskId);
  }
}
