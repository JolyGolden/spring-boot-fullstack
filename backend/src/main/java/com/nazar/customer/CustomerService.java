package com.nazar.customer;

import com.nazar.exeption.DuplicateResourceException;
import com.nazar.exeption.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer getCustomer(Integer id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id [%s] not found".formatted(id)));
    }

    public void addCustomer(Customer customer) {
        boolean emailExists = customerRepo.existsCustomerByEmail(customer.getEmail());
        if (emailExists) {
            throw new DuplicateResourceException("A customer with email " + customer.getEmail() + " already exists.");
        }
        customerRepo.save(customer);
    }

    public void deleteCustomer(Integer id) {
        boolean exists = customerRepo.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Customer with ID " + id + " not found");
        }
        customerRepo.deleteById(id);
    }

    public void updateCustomer(Integer id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
        if (updatedCustomer.getName() != null) {
            existingCustomer.setName(updatedCustomer.getName());
        }
        if (updatedCustomer.getEmail() != null) {
            existingCustomer.setEmail(updatedCustomer.getEmail());
        }
        if (updatedCustomer.getAge() != null) {
            existingCustomer.setAge(updatedCustomer.getAge());
        }
        customerRepo.save(existingCustomer);
    }
}
