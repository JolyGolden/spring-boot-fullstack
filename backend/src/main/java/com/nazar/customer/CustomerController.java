package com.nazar.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Get all customers
    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    // Get customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Integer id) {
        Customer customer = customerService.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

    // Add a new customer
    @PostMapping
    public void addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
    }

    // Delete a customer by ID
    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Integer id){
        customerService.deleteCustomer(id);
    }

    // Update an existing customer
    @PutMapping("/{customerId}")
    public void updateCustomer(@PathVariable("customerId") Integer id, @RequestBody Customer updatedCustomer){
        customerService.updateCustomer(id, updatedCustomer);
    }
}

