package ru.gb.junit;

public class CalculatorHistory {

  private final Storage storage;

  public CalculatorHistory(Storage storage) {
    this.storage = storage;
  }

  public void logSumOperation(int a, int b, int result) {
    if (!storage.save(String.format("%s + %s = %s", a, b, result))) {
      throw new RuntimeException("can't log sum operation");
    }
  }

}
