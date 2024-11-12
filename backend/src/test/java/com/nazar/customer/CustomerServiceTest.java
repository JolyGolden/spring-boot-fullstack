package com.nazar.customer;

import com.nazar.exeption.DuplicateResourceException;
import com.nazar.exeption.ResourceNotFoundException;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepo customerRepo;
    private CustomerService underTest;


    @BeforeEach
    void setUp() {

    underTest = new CustomerService(customerRepo);
    }



    @Test
    void getAllCustomers() {
        // Given
        List<Customer> expectedCustomers = List.of(
                new Customer(1, "Kateryna Sadovska", "katerynasadovska@gmail.com", 20, Gender.MALE),
                new Customer(2, "Nazar Zhanabergenov", "nazarzhanabergenov@gmail.com", 22, Gender.MALE)
        );
        Mockito.when(customerRepo.findAll()).thenReturn(expectedCustomers);

        // When
        List<Customer> actualCustomers = underTest.getAllCustomers();

        // Then
        assertEquals(expectedCustomers, actualCustomers,
                "The list of customers should match the expected result.");
        Mockito.verify(customerRepo).findAll();
    }
    @Test
    void canGetCustomer() {

        //Given
        int id = 1;
        Customer customer = new Customer(id, "Nazar Zhanabergenov", "nazarzhanabergenov@gmail.com", 22, Gender.MALE);
        Mockito.when(customerRepo.findById(id)).thenReturn(Optional.of(customer));
        //When
        Customer result = underTest.getCustomer(id);

        //Then
        assertEquals(customer,result);
        Mockito.verify(customerRepo).findById(id);

    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {

        //Given
        int id = 1;
        Mockito.when(customerRepo.findById(id)).thenReturn(Optional.empty());
        //When Then
        assertThrows(ResourceNotFoundException.class,() -> underTest.getCustomer(id));
        Mockito.verify(customerRepo).findById(id);

    }


    @Test
    void addCustomer() {
        //Given
        Customer customer = new Customer(
          null, "Micky Pearson","mickypearson@gmail.com",50, Gender.MALE
        );
        Mockito.when(customerRepo.existsCustomerByEmail(customer.getEmail())).thenReturn(false);

        //When
        underTest.addCustomer(customer);

        //Then
        Mockito.verify(customerRepo).existsCustomerByEmail(customer.getEmail());
        Mockito.verify(customerRepo).save(customer);
    }

    @Test
    void addCustomerThrowsException() {
        //Given
        Customer customer = new Customer(
                null, "Micky Pearson","mickypearson@gmail.com",50, Gender.MALE
        );
        Mockito.when(customerRepo.existsCustomerByEmail(customer.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> underTest.addCustomer(customer));
        Mockito.verify(customerRepo).existsCustomerByEmail(customer.getEmail());
        Mockito.verify(customerRepo, Mockito.never()).save(customer);
    }

    @Test
    void deleteCustomer() {
        //Given
        int id = 1;
        Mockito.when(customerRepo.existsById(id)).thenReturn(true);

        //When
        underTest.deleteCustomer(id);
        //Then
        Mockito.verify(customerRepo).existsById(id);
        Mockito.verify(customerRepo).deleteById(id);
    }

    @Test
    void deleteCustomerThrowsException() {
        //Given
        int id = 1;
        Mockito.when(customerRepo.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.deleteCustomer(id));
        Mockito.verify(customerRepo).existsById(id);
        Mockito.verify(customerRepo, Mockito.never()).deleteById(id);
    }

    @Test
    void updateCustomerNameOnly() {
        // Given
        int id = 1;
        Customer existingCustomer = new Customer(id, "John Doe", "john@example.com", 25, Gender.MALE);
        Mockito.when(customerRepo.findById(id)).thenReturn(Optional.of(existingCustomer));

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Jane Doe");

        // When
        underTest.updateCustomer(id, updatedCustomer);

        // Then
        assertEquals("Jane Doe", existingCustomer.getName());
        assertEquals("john@example.com", existingCustomer.getEmail());
        assertEquals(25, existingCustomer.getAge());
        Mockito.verify(customerRepo).save(existingCustomer);
    }

    @Test
    void updateCustomerEmailOnly() {
        // Given
        int id = 1;
        Customer existingCustomer = new Customer(id, "John Doe", "john@example.com", 25, Gender.MALE);
        Mockito.when(customerRepo.findById(id)).thenReturn(Optional.of(existingCustomer));

        Customer updatedCustomer = new Customer();
        updatedCustomer.setEmail("jane@example.com");

        // When
        underTest.updateCustomer(id, updatedCustomer);

        // Then
        assertEquals("John Doe", existingCustomer.getName());
        assertEquals("jane@example.com", existingCustomer.getEmail());
        assertEquals(25, existingCustomer.getAge());
        Mockito.verify(customerRepo).save(existingCustomer);
    }

    @Test
    void updateCustomerAgeOnly() {
        // Given
        int id = 1;
        Customer existingCustomer = new Customer(id, "John Doe", "john@example.com", 25, Gender.MALE);
        Mockito.when(customerRepo.findById(id)).thenReturn(Optional.of(existingCustomer));

        Customer updatedCustomer = new Customer();
        updatedCustomer.setAge(30);

        // When
        underTest.updateCustomer(id, updatedCustomer);

        // Then
        assertEquals("John Doe", existingCustomer.getName());
        assertEquals("john@example.com", existingCustomer.getEmail());
        assertEquals(30, existingCustomer.getAge());
        Mockito.verify(customerRepo).save(existingCustomer);
    }

    @Test
    void updateMultipleFields() {
        // Given
        int id = 1;
        Customer existingCustomer = new Customer(id, "John Doe", "john@example.com", 25, Gender.MALE);
        Mockito.when(customerRepo.findById(id)).thenReturn(Optional.of(existingCustomer));

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Jane Doe");
        updatedCustomer.setEmail("jane@example.com");
        updatedCustomer.setAge(30);

        // When
        underTest.updateCustomer(id, updatedCustomer);

        // Then
        assertEquals("Jane Doe", existingCustomer.getName());
        assertEquals("jane@example.com", existingCustomer.getEmail());
        assertEquals(30, existingCustomer.getAge());
        Mockito.verify(customerRepo).save(existingCustomer);
    }

    @Test
    void updateNonExistentCustomer() {
        // Given
        int id = 1;
        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Jane Doe");

        Mockito.when(customerRepo.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> underTest.updateCustomer(id, updatedCustomer));
    }

}