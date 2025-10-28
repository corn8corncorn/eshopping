package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public List<Customer> getAll() {
        return customerDAO.getAll();
    }

    @Override
    public Customer getById(Long id) {
        return customerDAO.getById(id);
    }

    @Override
    public Customer getByUser(User user) {
        return customerDAO.getByUser(user);
    }

    @Override
    public void save(Customer customer) {
        customerDAO.save(customer);
    }

    @Override
    public void update(Long id, Customer customer) {
        Customer existingCustomer = customerDAO.getById(id);
        if (existingCustomer != null) {
            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setPhone(customer.getPhone());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setCity(customer.getCity());
            existingCustomer.setPostalCode(customer.getPostalCode());
            existingCustomer.setCountry(customer.getCountry());
            customerDAO.save(existingCustomer);
        }
    }

    @Override
    public void delete(Long id) {
        customerDAO.delete(id);
    }

    @Override
    public Customer createCustomerForUser(User user, String fullName) {
        Customer customer = new Customer(user, fullName);
        customerDAO.save(customer);
        return customer;
    }
}
