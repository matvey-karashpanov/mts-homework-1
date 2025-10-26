package com.mipt.ailanakaramchakova.collectionsclasses;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Custom implementation of a resizable array list
 *
 * @param <A> the type of elements in this list
 */
public class CustomArrayList<A> implements CustomList<A> {

  private int capacity;
  private int size;
  private Object[] array;

  /**
   * Constructs an empty list with an initial capacity of {@code DEFAULT_CAPACITY}
   */
  public CustomArrayList() {
    capacity = 10;
    size = 0;
    array = new Object[capacity];
  }

  /**
   * Appends the specified element to the end of this list. The list capacity is automatically
   * increased if necessary
   *
   * @param value element to be appended to this list
   * @throws IllegalArgumentException if the specified element is null
   */
  @Override
  public void add(Object value) {
    if (value == null) {
      throw new IllegalArgumentException("Element can't be null");
    }

    if (size >= capacity) {
      capacity *= 1.5;
      Object[] newArray = new Object[capacity];

      for (int i = 0; i < size; i++) {
        newArray[i] = array[i];
      }

      array = newArray;
    }

    array[size] = value;
    size++;
  }

  /**
   * Returns the element at the specified position in this list
   *
   * @param index index of the element to return
   * @return the element at the specified index in this list
   * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
   */
  public A get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(
          "Index is out of range. Index: " + index + ". Size: " + size);
    }

    return (A) array[index];
  }

  /**
   * Removes the element at the specified position in this list. Shifts any subsequent elements to
   * the left (subtracts one from their indices).
   *
   * @param index the position of the element to be removed
   * @return the element that was removed from the list
   * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
   */
  @Override
  public A remove(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(
          "Index is out of range. Index: " + index + ". Size: " + size);
    }

    A removedValue = (A) array[index];

    for (int i = index; i < size - 1; i++) {
      array[i] = array[i + 1];
    }

    array[size - 1] = null;
    size--;

    return removedValue;
  }

  /**
   * @return the number of elements in this list
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Returns {@code true} if this list contains no elements.
   *
   * @return {@code true} if this list contains no elements
   */
  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns an iterator over elements of type {@code A}.
   *
   * @return an Iterator.
   */
  public Iterator<A> iterator() {
    return new CustomArrayListIterator();
  }

  /**
   * Iterator for CustomArrayList that supports element removal.
   */
  private class CustomArrayListIterator implements Iterator<A> {

    private int currentIndex = 0;
    private boolean removeAllowed = false;

    /**
     * Checks if there are more elements to iterate.
     *
     * @return true if more elements exist
     */
    @Override
    public boolean hasNext() {
      return currentIndex < size;
    }

    /**
     * Returns the next element in iteration.
     *
     * @return next element
     * @throws NoSuchElementException if no more elements
     */
    @Override
    public A next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      A element = (A) array[currentIndex];
      currentIndex++;
      removeAllowed = true;
      return element;
    }

    /**
     * Removes last element returned by next().
     *
     * @throws IllegalStateException if next() wasn't called or remove() already called
     */
    @Override
    public void remove() {
      if (!removeAllowed) {
        throw new IllegalStateException("remove() can only be called once after next()");
      }

      CustomArrayList.this.remove(currentIndex - 1);
      currentIndex--;
      removeAllowed = false;
    }
  }
}
