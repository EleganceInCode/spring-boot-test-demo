package ru.gb.app.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.app.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService service;

  @GetMapping
  public List<CustomerResponse> getAll() {
    return service.getAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
    return service.findById(id)
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerResponse request) {
        if (request.getName() == null || request.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        CustomerResponse created = service.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (service.deleteCustomer(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
