package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Customer;

public interface CustomerDAO {

	List<Customer> findAll();
	
	Customer findById(Long id);
	
	void save(Customer customer);
	
	void delete(Long id);
}
