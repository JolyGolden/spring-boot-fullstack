package com.nazar.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers ;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream().filter(c-> c.getId().equals(id)).findFirst();
    }



}
