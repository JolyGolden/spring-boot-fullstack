package com.nazar.customer;

import com.github.javafaker.Faker;
import com.nazar.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepoTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepo underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        String email = Faker.instance().internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                null, // Assuming the ID is auto-generated
                Faker.instance().name().fullName(),
                email,
                30
        );
        // Save the customer to the repository
        underTest.save(customer);

        // Check if the repository finds the customer by the generated email
        boolean exists = underTest.existsCustomerByEmail(email);

        // Assert that the customer exists
        assertTrue(exists, "Customer should exist by email after being saved");

    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        String email = Faker.instance().internet().emailAddress() + "-" + UUID.randomUUID();

        // Save the customer to the repository


        // Check if the repository finds the customer by the generated email
        boolean exists = underTest.existsCustomerByEmail(email);

        // Assert that the customer exists
        assertFalse(exists, "Customer should not exist by email if it was never saved");

    }

    @Test
    void existsCustomerById() {
        String email = Faker.instance().internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                null, // Assuming the ID is auto-generated
                Faker.instance().name().fullName(),
                email,
                30
        );
        Customer savedCustomer = underTest.save(customer);
        Integer customerId = savedCustomer.getId();


        boolean exists = underTest.existsCustomerById(customerId);


        assertTrue(exists, "Customer should exist by ID after being saved");


    }
    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        String email = Faker.instance().internet().emailAddress() + "-" + UUID.randomUUID();

        int id = -1;


        boolean exists = underTest.existsCustomerById(id);


        assertFalse(exists, "Customer should not exist by ID if it has not been saved");


    }
}