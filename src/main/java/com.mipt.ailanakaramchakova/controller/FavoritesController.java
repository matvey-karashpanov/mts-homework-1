package com.mipt.ailanakaramchakova.controller;

import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.service.FavoritesService;
import com.mipt.ailanakaramchakova.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Favorite Tasks.
 */
@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorites", description = "Operations for managing favorite tasks")
public class FavoritesController {

  private static final Logger logger = LoggerFactory.getLogger(FavoritesController.class);

  private final FavoritesService favoritesService;
  private final TaskService taskService;

  public FavoritesController(FavoritesService favoritesService, TaskService taskService) {
    this.favoritesService = favoritesService;
    this.taskService = taskService;
  }

  @PostMapping("/{taskId}")
  @Operation(summary = "Add to favorites", description = "Adds a task to favorites")
  public ResponseEntity<Void> addToFavorites(@PathVariable Long taskId) {
    favoritesService.addToFavorites(taskId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{taskId}")
  @Operation(summary = "Remove from favorites", description = "Removes a task from favorites")
  public ResponseEntity<Void> removeFromFavorites(@PathVariable Long taskId) {
    favoritesService.removeFromFavorites(taskId);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  @Operation(summary = "Get favorites", description = "Returns all favorite tasks")
  public ResponseEntity<List<TaskResponseDto>> getFavorites() {
    List<Long> favoriteIds = favoritesService.getFavorites();
    List<TaskResponseDto> favorites = new ArrayList<>();

    for (Long id : favoriteIds) {
      try {
        TaskResponseDto taskDto = taskService.findById(id);
        favorites.add(taskDto);
      } catch (Exception e) {
        logger.warn("Task {} not found in favorites", id);
      }
    }

    return ResponseEntity.ok(favorites);
  }
}
