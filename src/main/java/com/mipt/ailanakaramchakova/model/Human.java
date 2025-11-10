package com.mipt.ailanakaramchakova.model;

public class Human {

  private String Firstname;
  private String Lastname;
  private int age;
  private boolean isWorking;

  public String getFirstname() {
    return Firstname;
  }

  public void setFirstname(String firstname) {
    Firstname = firstname;
  }

  public String getLastname() {
    return Lastname;
  }

  public void setLastname(String lastname) {
    Lastname = lastname;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public boolean isWorking() {
    return isWorking;
  }

  public void setWorking(boolean working) {
    isWorking = working;
  }
}
