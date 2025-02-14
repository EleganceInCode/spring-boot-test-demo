package ru.gb.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.gb.junit.Calculator;
import ru.gb.junit.CalculatorHistory;

class CalculatorTest {

  @Test
  void testSum() {
    CalculatorHistory calculatorHistoryMock = Mockito.mock(CalculatorHistory.class);

    Calculator calculator = new Calculator(calculatorHistoryMock);
    int sum = calculator.sum(2, 7);
    Assertions.assertEquals(9, sum);

    Mockito.verify(calculatorHistoryMock).logSumOperation(2, 7, 9);
  }

}