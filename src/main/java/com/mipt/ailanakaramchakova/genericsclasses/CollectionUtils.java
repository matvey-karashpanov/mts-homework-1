package com.mipt.ailanakaramchakova.genericsclasses;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

  public static <T> List<T> mergeLists(List<? extends T> list1,
      List<? extends T> list2) {
    List<T> merged = new ArrayList<>();

    if (list1 != null) {
      CollectionUtils.addAll(merged, list1);
    }

    if (list2 != null) {
      CollectionUtils.addAll(merged, list2);
    }

    return merged;
  }

  public static <T> void addAll(List<? super T> destination,
      List<? extends T> source) {
    if (destination == null || source == null) {
      return;
    }

    for (T item : source) {
      destination.add(item);
    }
  }
}
