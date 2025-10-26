package com.mipt.ailanakaramchakova.validationclass;

import com.mipt.ailanakaramchakova.validationclass.annotations.Email;
import com.mipt.ailanakaramchakova.validationclass.annotations.NotNull;
import com.mipt.ailanakaramchakova.validationclass.annotations.Range;
import com.mipt.ailanakaramchakova.validationclass.annotations.Size;

public class TestUser {

  @NotNull(message = "The name cannot be null")
  @Size(min = 2, max = 50, message = "The name must be between 2 and 50 characters long")
  private String name;

  @Email
  @NotNull(message = "Email cannot be null")
  private String email;

  @Range(min = 0, max = 150, message = "The age should be between 0 and 150")
  private Integer age;

  @Size(min = 6, max = 20, message = "The password must be between 6 and 20 characters long")
  private String password;

  public TestUser(String name, String email, Integer age, String password) {
    this.name = name;
    this.email = email;
    this.age = age;
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Integer getAge() {
    return age;
  }

  public String getPassword() {
    return password;
  }
}
