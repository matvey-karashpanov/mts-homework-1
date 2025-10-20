package com.mipt.ailanakaramchakova.collectionsclasses;

/**
 * Interface for custom list
 *
 * @param <A> type of elements in list
 */
public interface CustomList<A> {

  /**
   * Add the element to the end of the list
   *
   * @param value the element to add
   * @throws IllegalArgumentException if element is null
   */
  void add(A value);

  /**
   * Get the element by the index
   *
   * @param index the index of element in the list
   * @return the element at the specified index
   * @throws IndexOutOfBoundsException if the index goes beyond the boundaries of the list
   */
  A get(int index);

  /**
   * Remove the element at the index. The indexes of the elements after this are reduced by one
   *
   * @param index the index of the element to delete
   * @return the element that was removed from the list
   * @throws IndexOutOfBoundsException if the index goes beyond the boundaries of the list
   */
  A remove(int index);

  /**
   * @return the number of elements in the list
   */
  int size();

  /**
   * Checks if the list is empty
   *
   * @return {@code true} if the list is empty, otherwise {@code false}
   */
  boolean isEmpty();
}
