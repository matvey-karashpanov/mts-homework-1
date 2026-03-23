package com.mipt.ailanakaramchakova.dto;

import com.mipt.ailanakaramchakova.model.Priority;
import com.mipt.ailanakaramchakova.validation.OnUpdate;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for updating an existing task.
 */
public class TaskUpdateDto {

  @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters", groups = OnUpdate.class)
  private String title;

  @Size(max = 500, message = "Description must not exceed 500 characters", groups = OnUpdate.class)
  private String description;

  private Boolean completed;

  private LocalDate dueDate;

  private Priority priority;

  @Size(max = 5, message = "Maximum 5 tags allowed", groups = OnUpdate.class)
  private Set<String> tags;

  public TaskUpdateDto() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }
}
