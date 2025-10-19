package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Customer;

public interface CustomerService {
    
    List<Customer> getAllCustomers();
    
    Customer getCustomerById(Long id);
    
    void saveCustomer(Customer customer);
    
    void updateCustomer(Long id, Customer updatedCustomer);
    
    void deleteCustomer(Long id);
}
