package ru.gb.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer")
@Data
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  public static Customer ofName(String name) {
    Customer customer = new Customer();
    customer.setName(name);
    return customer;
  }

}
