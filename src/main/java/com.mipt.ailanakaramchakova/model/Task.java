package com.mipt.ailanakaramchakova.model;

/**
 * Represents a task in the todo list.
 * Contains id, title, description and completed status.
 */
public class Task {

  private Long id;
  private String title;
  private String description;
  private boolean completed;

  public Task() {
  }

  public Task(Long id, String title, String description, boolean completed) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.completed = completed;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Task task = (Task) o;
    if (id == null && task.id == null) {
      return true;
    }
    if (id == null || task.id == null) {
      return false;
    }
    return completed == task.completed && id.equals(task.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Task{" +
      "id=" + id +
      ", title='" + title + '\'' +
      ", description='" + description + '\'' +
      ", completed=" + completed +
      '}';
  }
}
