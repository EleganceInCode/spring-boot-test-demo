package ru.gb.beans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.transaction.BeforeTransaction;

//@SpringBootTest(classes = {
//  AutoService.class,
//  Auto.class
//})

@SpringBootTest(classes = AutoServiceTest.TestConfig.class)
class AutoServiceTest {

  @TestConfiguration
  static class TestConfig {
    @Bean
    AutoService autoService(Auto auto) {
      return new AutoService(auto);
    }

    @Bean
    Auto auto() {
      return Mockito.mock(Auto.class);
    }
  }

  @Autowired
  AutoService autoService;
  @SpyBean
  Auto auto;

  @Test
  void testChangeWheelsInOk() {
    Mockito.doNothing().when(auto).changeWheels();
    Assertions.assertTrue(autoService.changeWheelsIn());
  }

  @Test
  void testChangeWheelsInError() {
    Mockito.doThrow(RuntimeException.class).when(auto).changeWheels();
    Assertions.assertFalse(autoService.changeWheelsIn());
  }

}