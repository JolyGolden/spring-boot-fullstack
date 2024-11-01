package com.nazar.journey;

import com.github.javafaker.Faker;
import com.nazar.customer.Customer;
import com.nazar.customer.CustomerRegistrationRequest;
import com.nazar.customer.CustomerUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest  {

    @Autowired
    private WebTestClient webTestClient;
    private static final String CUSTOMER_URI = "/api/v1/customers";
    private static final Random RANDOM = new Random();


    @Test
    void canRegisterACustomer() {
        // create a registration request

        Faker faker = new Faker();

        String name = faker.name().fullName();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = lastName + "-" + UUID.randomUUID() + "gmail.com";
        int age = RANDOM.nextInt(1, 100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        //send a post request

        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //get all customers

        List<Customer> allCustomers =  webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer();
        expectedCustomer.setName(name);
        expectedCustomer.setEmail(email);
        expectedCustomer.setAge(age);

        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        int id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email))
                        .map(Customer::getId).findFirst().orElseThrow();

        expectedCustomer.setId(id);

        //get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        // create a registration request

        Faker faker = new Faker();

        String name = faker.name().fullName();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = lastName + "-" + UUID.randomUUID() + "gmail.com";
        int age = RANDOM.nextInt(1, 100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        //send a post request

        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //get all customers

        List<Customer> allCustomers =  webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        //make sure customer is present


        int id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();


        //delete customer
       webTestClient.delete()
                       .uri(CUSTOMER_URI + "/{id}", id)
                               .accept(MediaType.APPLICATION_JSON)
                                       .exchange()
                                               .expectStatus()
                                                       .isOk();

        //get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create a registration request

        Faker faker = new Faker();

        String name = faker.name().fullName();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = lastName + "-" + UUID.randomUUID() + "gmail.com";
        int age = RANDOM.nextInt(1, 100);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        //send a post request

        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        //get all customers

        List<Customer> allCustomers =  webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        //make sure customer is present


        int id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();



        //update customer
        String newName = "Ali";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName, null, null
        );
        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();
        Customer expected = new Customer(
                id,newName,email,age
        );
        assertThat(updatedCustomer).isEqualTo(expected);
    }
}
