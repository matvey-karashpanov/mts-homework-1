package com.mipt.ailanakaramchakova.collectionsclasses;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomArrayListTest {

  private CustomList<Integer> list; // Изменил на Integer для типобезопасности

  @BeforeEach
  void setUp() {
    list = new CustomArrayList<>();
  }

  @Test
  void add() {
    for (int i = 0; i < 10; i++) {
      list.add(i);
    }

    assertEquals(10, list.size());
    assertEquals(0, list.get(0));
    assertEquals(9, list.get(9));
  }

  @Test
  void get() {
    for (int i = 0; i < 10; i++) {
      list.add(i);
    }

    assertEquals(0, list.get(0));
    assertEquals(9, list.get(9));

    assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(11));
  }

  @Test
  void remove() {
    for (int i = 0; i < 10; i++) {
      list.add(i);
    }

    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(11));

    Integer removed = list.remove(5);
    assertEquals(5, removed);
    assertEquals(9, list.size());
    assertEquals(0, list.get(0));
    assertEquals(9, list.get(8)); // Элемент 9 теперь на позиции 8
  }

  @Test
  void size() {
    assertEquals(0, list.size());
    for (int i = 0; i < 10; i++) {
      list.add(i);
    }

    assertEquals(10, list.size());
    list.remove(9);
    assertEquals(9, list.size());
  }

  @Test
  void isEmpty() {
    assertTrue(list.isEmpty());
    list.add(0);
    assertFalse(list.isEmpty());
    list.remove(0);
    assertTrue(list.isEmpty());
  }

  @Test
  void iteratorHasNext() {
    CustomArrayList<Integer> list = new CustomArrayList<>();
    for (int i = 0; i < 4; i++) {
      list.add(i);
    }

    Iterator<Integer> it = list.iterator();

    assertTrue(it.hasNext());
    assertEquals(0, it.next());

    assertTrue(it.hasNext());
    assertEquals(1, it.next());

    assertTrue(it.hasNext());
    assertEquals(2, it.next());

    assertTrue(it.hasNext());
    assertEquals(3, it.next());

    assertFalse(it.hasNext());
  }

  @Test
  void iteratorRemove() {
    CustomArrayList<Integer> list = new CustomArrayList<>();
    list.add(10);
    list.add(20);
    list.add(30);
    list.add(40);

    Iterator<Integer> it = list.iterator();

    assertEquals(10, it.next());
    it.remove();

    assertEquals(3, list.size());
    assertEquals(20, list.get(0));
    assertEquals(30, list.get(1));
    assertEquals(40, list.get(2));
  }

  @Test
  void iteratorRemoveMultipleCalls() {
    CustomArrayList<Integer> list = new CustomArrayList<>();
    list.add(10);
    list.add(20);

    Iterator<Integer> it = list.iterator();
    it.next();
    it.remove();

    assertThrows(IllegalStateException.class, () -> it.remove());
  }

  @Test
  void iteratorNextOnEmptyList() {
    CustomArrayList<Integer> list = new CustomArrayList<>();
    Iterator<Integer> it = list.iterator();

    assertThrows(NoSuchElementException.class, () -> it.next());
  }

  @Test
  void testCapacityExpansion() {
    for (int i = 0; i < 15; i++) {
      list.add(i);
    }

    assertEquals(15, list.size());
  }

  @Test
  void removeFirstElement() {
    list.add(10);
    list.add(20);
    list.add(30);

    Integer removed = list.remove(0);
    assertEquals(10, removed);
    assertEquals(2, list.size());
    assertEquals(20, list.get(0));
    assertEquals(30, list.get(1));
  }

  @Test
  void removeLastElement() {
    list.add(10);
    list.add(20);
    list.add(30);

    Integer removed = list.remove(2);
    assertEquals(30, removed);
    assertEquals(2, list.size());
    assertEquals(10, list.get(0));
    assertEquals(20, list.get(1));
  }
}
