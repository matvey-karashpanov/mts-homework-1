package com.mipt.ailanakaramchakova.collectionsclasses;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CollectionPerformanceTester {

  @Test
  public void compareArrayListAndLinkedList() {
    int n = 10000;

    System.out.println("Операция                ArrayList   LinkedList");

    List<Integer> list = new ArrayList<>();
    long start = System.currentTimeMillis();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    long timeOne = System.currentTimeMillis() - start;

    list = new LinkedList<>();
    start = System.currentTimeMillis();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    long timeTwo = System.currentTimeMillis() - start;

    System.out.println("Добавление в конец      " + timeOne + " ms       " + timeTwo + " ms");

    list = new ArrayList<>();
    start = System.currentTimeMillis();
    for (int i = 0; i < n; i++) {
      list.addFirst(i);
    }
    timeOne = System.currentTimeMillis() - start;

    list = new LinkedList<>();
    start = System.currentTimeMillis();
    for (int i = 0; i < n; i++) {
      list.addFirst(i);
    }
    timeTwo = System.currentTimeMillis() - start;

    System.out.println("Добавление в начало     " + timeOne + " ms       " + timeTwo + " ms");

    list = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    start = System.currentTimeMillis();
    for (int i = 0; i < 100; i++) {
      list.add(n / 2, 0);
    }
    timeOne = System.currentTimeMillis() - start;

    list = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    start = System.currentTimeMillis();
    for (int i = 0; i < 100; i++) {
      list.add(n / 2, 0);
    }
    timeTwo = System.currentTimeMillis() - start;

    System.out.println("Вставка в середину      " + timeOne + " ms       " + timeTwo + " ms");

    // 4. Доступ по индексу
    list = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    start = System.currentTimeMillis();
    int sum = 0;
    for (int i = 0; i < n; i++) {
      sum += list.get(i);
    }
    timeOne = System.currentTimeMillis() - start;

    list = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    start = System.currentTimeMillis();
    sum = 0;
    for (int i = 0; i < n; i++) {
      sum += list.get(i);
    }
    timeTwo = System.currentTimeMillis() - start;

    System.out.println("Доступ по индексу       " + timeOne + " ms       " + timeTwo + " ms");

    list = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    start = System.currentTimeMillis();
    while (!list.isEmpty()) {
      list.removeFirst();
    }
    timeOne = System.currentTimeMillis() - start;

    list = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    start = System.currentTimeMillis();
    while (!list.isEmpty()) {
      list.removeFirst();
    }
    timeTwo = System.currentTimeMillis() - start;

    System.out.println("Удаление из начала      " + timeOne + " ms       " + timeTwo + " ms");

    list = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    start = System.currentTimeMillis();
    while (!list.isEmpty()) {
      list.removeLast();
    }
    timeOne = System.currentTimeMillis() - start;

    list = new LinkedList<>();
    for (int i = 0; i < n; i++) {
      list.add(i);
    }
    start = System.currentTimeMillis();
    while (!list.isEmpty()) {
      list.removeLast();
    }
    timeTwo = System.currentTimeMillis() - start;

    System.out.println("Удаление из конца       " + timeOne + " ms       " + timeTwo + " ms");
  }
}
