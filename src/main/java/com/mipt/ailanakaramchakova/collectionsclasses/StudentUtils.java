package com.mipt.ailanakaramchakova.collectionsclasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StudentUtils {

  public static void main(String[] args) {
    Map<Integer, Student> hashMap = new HashMap<>();
    Map<Integer, Student> treeMap = new TreeMap<>(Collections.reverseOrder());
  }

  public static List<Student> findStudentsByGradeRange(Map<Integer, Student> map, double minGrade,
      double maxGrade) {
    List<Student> result = new ArrayList<>();
    for (Student student : map.values()) {
      if (student.getGrade() >= minGrade && student.getGrade() <= maxGrade) {
        result.add(student);
      }
    }
    return result;
  }

  public static List<Student> getTopNStudents(TreeMap<Integer, Student> map, int n) {
    List<Student> result = new ArrayList<>();
    int count = 0;

    for (Student student : map.values()) {
      if (count >= n) {
        break;
      }
      result.add(student);
      count++;
    }

    return result;
  }
}
