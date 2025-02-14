package ru.gb.app.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.gb.JUnitSpringBootBase;
import ru.gb.app.model.Customer;
import ru.gb.app.repository.CustomerRepository;

import java.util.List;
import java.util.Objects;

class CustomerControllerTest extends JUnitSpringBootBase {

  // TDD -> TestDrivenDevelopment
  // DataJpaTest
  // Testcontainers -> docker containers
  //

  @Autowired
  WebTestClient webTestClient;
  @Autowired
  CustomerRepository customerRepository;
  @Autowired
  JdbcTemplate jdbcTemplate;

  @Data
  static class JUnitCustomerResponse {
    private Long id;
    private String name;
  }

  @Test
  void testFindByIdSuccess() {
    // подготовил данные
    Customer expected = customerRepository.save(Customer.ofName("random"));

    JUnitCustomerResponse responseBody = webTestClient.get()
      .uri("/api/customer/" + expected.getId())
      .exchange()
      .expectStatus().isOk()
      .expectBody(JUnitCustomerResponse.class)
      .returnResult().getResponseBody();

    Assertions.assertNotNull(responseBody);
    Assertions.assertEquals(expected.getId(), responseBody.getId());
    Assertions.assertEquals(expected.getName(), responseBody.getName());
  }

  @Test
  void testFindByIdNotFound() {
    Long maxId = jdbcTemplate.queryForObject("select max(id) from customer", Long.class);

    webTestClient.get()
      .uri("/api/customer/" + maxId + 1)
      .exchange()
      .expectStatus().isNotFound();
  }

  @Test
  void testGetAll() {
    // подготовил данные
    customerRepository.saveAll(List.of(
      Customer.ofName("first"),
      Customer.ofName("second")
    ));

    List<Customer> expected = customerRepository.findAll();

    List<JUnitCustomerResponse> responseBody = webTestClient.get()
      .uri("/api/customer")
      // .retrieve
      .exchange()
      .expectStatus().isOk()
      .expectBody(new ParameterizedTypeReference<List<JUnitCustomerResponse>>() {
      })
      .returnResult()
      .getResponseBody();

    Assertions.assertEquals(expected.size(), responseBody.size());
    for (JUnitCustomerResponse customerResponse : responseBody) {
      boolean found = expected.stream()
        .filter(it -> Objects.equals(it.getId(), customerResponse.getId()))
        .anyMatch(it -> Objects.equals(it.getName(), customerResponse.getName()));
      Assertions.assertTrue(found);
    }
  }

  @Test
  @Disabled
  void testSave() {
    JUnitCustomerResponse request = new JUnitCustomerResponse();

    JUnitCustomerResponse responseBody = webTestClient.post()
      .uri("/api/customer")
      .bodyValue(request)
      .exchange()
      .expectStatus().isCreated()
      .expectBody(JUnitCustomerResponse.class)
      .returnResult().getResponseBody();

    Assertions.assertNotNull(responseBody);
    Assertions.assertNotNull(responseBody.getId());

    Assertions.assertTrue(customerRepository.findById(request.getId()).isPresent());
  }

    @Test
    void testCreateCustomer() {
        JUnitCustomerResponse request = new JUnitCustomerResponse();
        request.setName("New Customer");

        JUnitCustomerResponse response = webTestClient.post()
                .uri("/api/customer")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitCustomerResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals(request.getName(), response.getName());
    }

    @Test
    void testCreateCustomerBadRequest() {
        JUnitCustomerResponse request = new JUnitCustomerResponse();

        webTestClient.post()
                .uri("/api/customer")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testDeleteCustomer() {
        Customer customer = customerRepository.save(Customer.ofName("To Delete"));

        webTestClient.delete()
                .uri("/api/customer/" + customer.getId())
                .exchange()
                .expectStatus().isNoContent();

        Assertions.assertFalse(customerRepository.findById(customer.getId()).isPresent());
    }

    @Test
    void testDeleteCustomerNotFound() {
        Long invalidId = jdbcTemplate.queryForObject("select max(id) from customer", Long.class) + 1;

        webTestClient.delete()
                .uri("/api/customer/" + invalidId)
                .exchange()
                .expectStatus().isNotFound();
    }

}