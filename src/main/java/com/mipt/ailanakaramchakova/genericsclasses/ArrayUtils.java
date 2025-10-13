package com.mipt.ailanakaramchakova.genericsclasses;

public class ArrayUtils {

  public static <T> int findFirst(T[] array, T element) {
    if (array == null) {
      return -1;
    }

    for (int i = 0; i < array.length; i++) {
      if (element == null) {
        if (array[i] == null) {
          return i;
        }
      } else {
        if (element.equals(array[i])) {
          return i;
        }
      }
    }
    return -1;
  }
}
