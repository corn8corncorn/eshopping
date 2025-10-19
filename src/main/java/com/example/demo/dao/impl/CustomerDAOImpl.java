package com.example.demo.dao.impl;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<Customer> findAll() {
		return getCurrentSession().createQuery("FROM Customer", Customer.class).list();
	}

	@Override
	public Customer findById(Long id) {
        return getCurrentSession().get(Customer.class, id);
	}

	@Override
	public void save(Customer customer) {
		getCurrentSession().saveOrUpdate(customer);
	}

	@Override
	public void delete(Long id) {
		Customer customer = getCurrentSession().get(Customer.class, id);
		if (customer != null ) {
			getCurrentSession().delete(customer);
		}
	}
	
}
