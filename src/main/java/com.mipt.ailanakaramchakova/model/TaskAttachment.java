package com.mipt.ailanakaramchakova.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Represents a file attachment for a task.
 */
@Entity
@Table(name = "task_attachments")
public class TaskAttachment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id", nullable = false)
  private Task task;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "stored_file_name", nullable = false)
  private String storedFileName;

  @Column(name = "content_type")
  private String contentType;

  @Column(nullable = false)
  private long size;

  @CreationTimestamp
  @Column(name = "uploaded_at", updatable = false)
  private LocalDateTime uploadedAt;

  public TaskAttachment() {
  }

  public TaskAttachment(Long id, Task task, String fileName, String storedFileName,
    String contentType, long size, LocalDateTime uploadedAt) {
    this.id = id;
    this.task = task;
    this.fileName = fileName;
    this.storedFileName = storedFileName;
    this.contentType = contentType;
    this.size = size;
    this.uploadedAt = uploadedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getStoredFileName() {
    return storedFileName;
  }

  public void setStoredFileName(String storedFileName) {
    this.storedFileName = storedFileName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public LocalDateTime getUploadedAt() {
    return uploadedAt;
  }

  public void setUploadedAt(LocalDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }
}
