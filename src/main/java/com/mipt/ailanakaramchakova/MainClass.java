package com.mipt.ailanakaramchakova;

public class MainClass {

  private int privateIntField;
  private String privateStringField;
  protected static double protectedStaticDoubleField;
  public final long publicFinalLongField = 1_000_000;

  public static void main(String[] args) {
    for (int i = 0; i <= 15; i++) {
      System.out.println("Iter: " + i);
    }
  }
}
