package ru.gb.beans;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoService {

  private final Auto auto;

  public boolean changeWheelsIn() {
    try {
      auto.changeWheels();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

}
