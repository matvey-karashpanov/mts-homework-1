package com.mipt.ailanakaramchakova.multithreading;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;


public class BankTest {

  @Test
  public void testSendToAccountConcurrent() throws InterruptedException {
    Bank bank = new Bank();
    BankAccount bankAccountOne = new BankAccount(1, 1000);
    BankAccount bankAccountTwo = new BankAccount(2, 1000);

    int numberThreads = 10;
    int transferAmount = 10;
    ExecutorService executor = Executors.newFixedThreadPool(numberThreads);

    for (int i = 0; i < numberThreads; i++) {
      executor.submit(() -> bank.sendToAccount(bankAccountOne, bankAccountTwo, transferAmount));
    }

    executor.shutdown();
    assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

    assertEquals(1000 - numberThreads * transferAmount, bankAccountOne.getBalance());
    assertEquals(1000 + numberThreads * transferAmount, bankAccountTwo.getBalance());
  }

  @Test
  public void testSendToAccountDeadlockDetected() throws InterruptedException {
    Bank bank = new Bank();
    BankAccount bankAccountOne = new BankAccount(1, 1000);
    BankAccount bankAccountTwo = new BankAccount(2, 1000);

    Thread threadOne = new Thread(() -> bank.sendToAccountDeadlock(bankAccountOne, bankAccountTwo, 100));
    Thread threadTwo = new Thread(() -> bank.sendToAccountDeadlock(bankAccountTwo, bankAccountOne, 100));

    threadOne.start();
    threadTwo.start();

    threadOne.join(2000);
    threadTwo.join(2000);

    boolean deadlockOccurred = threadOne.isAlive() || threadTwo.isAlive();

    if (threadOne.isAlive()) {
      threadOne.interrupt();
    }
    if (threadTwo.isAlive()) {
      threadTwo.interrupt();
    }
    assertTrue(deadlockOccurred || (!threadOne.isAlive() && !threadTwo.isAlive()));
  }

  @Test
  public void testNullAccounts() {
    Bank bank = new Bank();
    BankAccount bankAccount = new BankAccount(1, 100);
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(null, bankAccount, 10));
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(bankAccount, null, 10));
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(null, null, 10));
  }

  @Test
  public void testNullAccountsDeadlock() {
    Bank bank = new Bank();
    BankAccount bankAccount = new BankAccount(1, 100);
    assertThrows(IllegalArgumentException.class,
        () -> bank.sendToAccountDeadlock(null, bankAccount, 10));
    assertThrows(IllegalArgumentException.class,
        () -> bank.sendToAccountDeadlock(bankAccount, null, 10));
  }

  @Test
  public void testNegativeAmount() {
    Bank bank = new Bank();
    BankAccount bankAccountOne = new BankAccount(1, 100);
    BankAccount bankAccountTwo = new BankAccount(2, 100);
    assertThrows(IllegalArgumentException.class,
        () -> bank.sendToAccount(bankAccountOne, bankAccountTwo, -10));
    assertThrows(IllegalArgumentException.class,
        () -> bank.sendToAccountDeadlock(bankAccountOne, bankAccountTwo, -10));
  }

  @Test
  public void testInsufficientFunds() {
    Bank bank = new Bank();
    BankAccount bankAccountOne = new BankAccount(1, 50);
    BankAccount bankAccountTwo = new BankAccount(2, 100);
    assertFalse(bank.sendToAccount(bankAccountOne, bankAccountTwo, 100));
    assertEquals(50, bankAccountOne.getBalance());
    assertEquals(100, bankAccountTwo.getBalance());
  }

  @Test
  public void testInsufficientFundsDeadlock() {
    Bank bank = new Bank();
    BankAccount bankAccountOne = new BankAccount(1, 50);
    BankAccount bankAccountTwo = new BankAccount(2, 100);
    assertFalse(bank.sendToAccountDeadlock(bankAccountOne, bankAccountTwo, 100));
    assertEquals(50, bankAccountOne.getBalance());
    assertEquals(100, bankAccountTwo.getBalance());
  }

  @Test
  public void testSendToSelf() {
    Bank bank = new Bank();
    BankAccount bankAccount = new BankAccount(1, 100);
    assertTrue(bank.sendToAccount(bankAccount, bankAccount, 50));
    assertEquals(100, bankAccount.getBalance());
  }

  @Test
  public void testSendZeroAmount() {
    Bank bank = new Bank();
    BankAccount bankAccountOne = new BankAccount(1, 100);
    BankAccount bankAccountTwo = new BankAccount(2, 100);
    assertTrue(bank.sendToAccount(bankAccountOne, bankAccountTwo, 0));
    assertEquals(100, bankAccountOne.getBalance());
    assertEquals(100, bankAccountTwo.getBalance());
  }

  @Test
  public void testSendToAccountDeadlockSuccess() {
    Bank bank = new Bank();
    BankAccount bankAccountOne = new BankAccount(1, 100);
    BankAccount bankAccountTwo = new BankAccount(2, 100);
    assertTrue(bank.sendToAccountDeadlock(bankAccountOne, bankAccountTwo, 10));
    assertEquals(90, bankAccountOne.getBalance());
    assertEquals(110, bankAccountTwo.getBalance());
  }

  @Test
  public void testDepositNegativeAmount() {
    BankAccount bankAccount = new BankAccount(1, 100);
    assertThrows(IllegalArgumentException.class, () -> bankAccount.deposit(-10));
    assertEquals(100, bankAccount.getBalance());
  }

  @Test
  public void testWithdrawNegativeAmount() {
    BankAccount bankAccount = new BankAccount(1, 100);
    assertThrows(IllegalArgumentException.class, () -> bankAccount.withdraw(-10));
    assertEquals(100, bankAccount.getBalance());
  }

  @Test
  public void testWithdrawEdgeCases() {
    BankAccount bankAccount = new BankAccount(1, 100);

    assertTrue(bankAccount.withdraw(0));
    assertEquals(100, bankAccount.getBalance());

    assertTrue(bankAccount.withdraw(100));
    assertEquals(0, bankAccount.getBalance());

    assertFalse(bankAccount.withdraw(1));
    assertEquals(0, bankAccount.getBalance());
  }
}
