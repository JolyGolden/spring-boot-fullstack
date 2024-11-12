package com.nazar.customer;

import com.github.javafaker.Faker;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepo customerRepo;

    @BeforeEach
    void setUp() {
     autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepo);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //When
        underTest.selectAllCustomers();

        //Then
        Mockito.verify(customerRepo).findAll();

    }

    @Test
    void existsCustomerWithEmail() {

        //Giver
        String email = "2qdfwd@maf.ru";
        //When
        underTest.existsCustomerWithEmail(email);
        //Then
        Mockito.verify(customerRepo).existsCustomerByEmail(email);
    }


    @Test
    void existsCustomerById() {
        Integer id = 1;
        underTest.existsCustomerById(id);
        Mockito.verify(customerRepo).existsCustomerById(id);
    }

    @Test
    void save() {
        Customer customer = new Customer(
                null,
                Faker.instance().name().fullName(),
                Faker.instance().internet().emailAddress(),
                25, Gender.MALE

        );
        underTest.save(customer);

        Mockito.verify(customerRepo).save(customer);
    }

    @Test
    void selectCustomerById() {
        //Given
        int id = 1;
        //When
        underTest.selectCustomerById(id);
        //Then
        Mockito.verify(customerRepo).findById(id);
    }
}