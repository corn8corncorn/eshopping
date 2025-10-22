package com.example.demo.dao.impl;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CustomerDAOImpl implements CustomerDAO {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public List<Customer> getAll() {
        // 查詢所有客戶
        return getCurrentSession().createQuery("FROM Customer", Customer.class).list();
    }
    
    @Override
    public Customer getById(Long id) {
        // 依 ID 取得客戶
        return getCurrentSession().get(Customer.class, id);
    }
    
    @Override
    public Customer getByUser(User user) {
        // 依 User 取得客戶
        Query<Customer> query = getCurrentSession().createQuery(
            "FROM Customer WHERE user = :user", Customer.class);
        query.setParameter("user", user);
        return query.uniqueResult();
    }
    
    @Override
    public void save(Customer customer) {
        // 新增或更新客戶
        getCurrentSession().saveOrUpdate(customer);
    }
    
    @Override
    public void delete(Long id) {
        // 依 ID 刪除客戶
        Customer customer = getCurrentSession().get(Customer.class, id);
        if (customer != null) {
            getCurrentSession().delete(customer);
        }
    }
}
