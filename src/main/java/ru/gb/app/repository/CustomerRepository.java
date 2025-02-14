package ru.gb.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.app.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
