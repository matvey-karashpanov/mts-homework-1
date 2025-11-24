package com.mipt.ailanakaramchakova.multithreading;

public class BankAccount {

  private final long id;
  private int balance;

  public BankAccount(long id, int initialBalance) {
    this.id = id;
    this.balance = initialBalance;
  }

  public long getId() {
    return id;
  }

  public int getBalance() {
    return balance;
  }

  public void deposit(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Deposit amount must be non-negative");
    }
    balance += amount;
  }

  public boolean withdraw(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Withdraw amount must be non-negative");
    }
    if (balance < amount) {
      return false;
    }
    balance -= amount;
    return true;
  }
}
