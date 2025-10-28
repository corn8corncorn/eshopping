package com.example.demo.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.User;

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
        // 查詢所有客戶，使用 JOIN FETCH 確保 User 物件被載入
        return getCurrentSession().createQuery("FROM Customer c JOIN FETCH c.user", Customer.class).list();
    }

    @Override
    public Customer getById(Long id) {
        // 依 ID 取得客戶，使用 JOIN FETCH 確保 User 物件被載入
        Query<Customer> query = getCurrentSession().createQuery(
            "FROM Customer c JOIN FETCH c.user WHERE c.id = :id", Customer.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    @Override
    public Customer getByUser(User user) {
        // 依 User 取得客戶，使用 JOIN FETCH 確保 User 物件被載入
        Query<Customer> query = getCurrentSession().createQuery(
            "FROM Customer c JOIN FETCH c.user WHERE c.user = :user", Customer.class);
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
