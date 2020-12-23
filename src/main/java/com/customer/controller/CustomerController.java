package com.customer.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.customer.domain.Customer;
import com.customer.repository.CustomerRepository;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(value = "/customers", produces = "application/json")
    public List<Customer> findAllCustomers() {
        return customerRepository.findAllCustomers();
    }

    @GetMapping(value = "/customers/{id}", produces = "application/json")
    public Customer findCustomerById(@PathVariable long id) {
        return customerRepository.findCustomerById(id);
    }

    @PostMapping(value = "/customers", consumes = "application/json", produces = "application/json")
    public void createCustomer(@RequestBody @Valid Customer customer) {
            System.out.println(new Customer (customer.getId(), customer.getFullName(), customer.getEmail(), customer.getPhone()));
            customerRepository.createCustomer(customer);
    }

    @PutMapping(value = "/customers/{id}", consumes = "application/json", produces = "application/json")
    public void updateCustomer(@RequestBody @Valid Customer customer, @PathVariable long id) {
        if (customerRepository.findById(id).isPresent()) {
            customerRepository.updateCustomer(customer, id);
        }
    }

    @DeleteMapping(value = "/customers/{id}", produces = "application/json")
    public void deleteCustomer(@PathVariable long id) {
        customerRepository.deleteCustomer(id);
    }

}
