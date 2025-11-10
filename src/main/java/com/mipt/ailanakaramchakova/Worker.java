package com.mipt.ailanakaramchakova;

public abstract class Worker {

  public abstract void work(int value);

  public boolean goHome(String stringOne, String stringTwo) {
    return stringOne.equals(stringTwo);
  }
}
