package ru.gb.junit;

public class Calculator {

  private final CalculatorHistory calculatorHistory;

  public Calculator(CalculatorHistory calculatorHistory) {
    this.calculatorHistory = calculatorHistory;
  }

  public int sum(int a, int b) {
    int result = a + b;
    calculatorHistory.logSumOperation(a, b, result);
    return result;
  }

}
