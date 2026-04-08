package com.mipt.ailanakaramchakova.service;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service for managing favorite tasks using session.
 */
@Service
public class FavoritesService {

  private static final String FAVORITES_ATTR = "favoriteTaskIds";

  private final HttpSession httpSession;

  public FavoritesService(HttpSession httpSession) {
    this.httpSession = httpSession;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getFavorites() {
    List<Long> favorites = (List<Long>) httpSession.getAttribute(FAVORITES_ATTR);
    if (favorites == null) {
      favorites = new ArrayList<>();
      httpSession.setAttribute(FAVORITES_ATTR, favorites);
    }
    return favorites;
  }

  public void addToFavorites(Long taskId) {
    List<Long> favorites = getFavorites();
    if (!favorites.contains(taskId)) {
      favorites.add(taskId);
    }
  }

  public void removeFromFavorites(Long taskId) {
    List<Long> favorites = getFavorites();
    favorites.remove(taskId);
  }

  public boolean isFavorite(Long taskId) {
    return getFavorites().contains(taskId);
  }
}
