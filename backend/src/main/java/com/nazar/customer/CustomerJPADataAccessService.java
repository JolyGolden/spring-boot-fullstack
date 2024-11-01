package com.nazar.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao{

    private final CustomerRepo customerRepo;

    public CustomerJPADataAccessService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepo.findAll();
    }

    public boolean existsCustomerWithEmail(String email){
        return customerRepo.existsCustomerByEmail(email);

    }

    public boolean existsCustomerById(Integer id){
        return customerRepo.existsCustomerById(id);

    }

    public void save(Customer customer) {
        customerRepo.save(customer);
    }
    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customerRepo.findById(id);
    }
}
