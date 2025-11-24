package com.mipt.ailanakaramchakova.multithreading;

public class Bank {

  public boolean sendToAccount(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Accounts must not be null");
    }

    if (from == to) {
      return true;
    }

    if (amount < 0) {
      throw new IllegalArgumentException("Amount must be non-negative");
    }

    BankAccount first;
    if (from.getId() < to.getId()) {
      first = from;
    } else {
      first = to;
    }

    BankAccount second;
    if (from.getId() < to.getId()) {
      second = to;
    } else {
      second = from;
    }

    synchronized (first) {
      synchronized (second) {
        if (from.getBalance() < amount) {
          return false;
        }
        from.withdraw(amount);
        to.deposit(amount);
        return true;
      }
    }
  }

  public boolean sendToAccountDeadlock(BankAccount from, BankAccount to, int amount) {
    if (from == null || to == null) {
      throw new IllegalArgumentException("Accounts must not be null");
    }

    if (from == to) {
      return true;
    }

    if (amount < 0) {
      throw new IllegalArgumentException("Amount must be non-negative");
    }

    synchronized (from) {
      synchronized (to) {
        if (from.getBalance() < amount) {
          return false;
        }
        from.withdraw(amount);
        to.deposit(amount);
        return true;
      }
    }
  }
}
