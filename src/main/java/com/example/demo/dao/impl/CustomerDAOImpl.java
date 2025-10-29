package com.example.demo.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.User;

/**
 * 客戶資料存取層實作類別
 * 負責與資料庫進行客戶相關的資料操作
 */
@Repository
@Transactional
public class CustomerDAOImpl implements CustomerDAO {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 取得目前的 Hibernate Session
     * 
     * @return 目前的 Hibernate Session
     */
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * 取得所有客戶
     * 使用 JOIN FETCH 確保 User 物件被載入，避免 N+1 查詢問題
     * 
     * @return 所有客戶的列表
     */
    @Override
    public List<Customer> getAll() {
        logger.debug("從資料庫查詢所有客戶");
        List<Customer> customers = getCurrentSession().createQuery("FROM Customer c JOIN FETCH c.user", Customer.class).list();
        logger.info("成功查詢到 {} 個客戶", customers.size());
        return customers;
    }

    /**
     * 依 ID 取得客戶
     * 使用 JOIN FETCH 確保 User 物件被載入
     * 
     * @param id 客戶 ID
     * @return 客戶物件，如果不存在則返回null
     */
    @Override
    public Customer getById(Long id) {
        logger.debug("從資料庫取得客戶 - customerId: {}", id);
        Query<Customer> query = getCurrentSession().createQuery(
            "FROM Customer c JOIN FETCH c.user WHERE c.id = :id", Customer.class);
        query.setParameter("id", id);
        Customer customer = query.uniqueResult();
        logger.debug("客戶查詢結果 - customerId: {}, found: {}", id, customer != null);
        return customer;
    }

    /**
     * 依 User 取得客戶
     * 使用 JOIN FETCH 確保 User 物件被載入
     * 
     * @param user 使用者物件
     * @return 客戶物件，如果不存在則返回null
     */
    @Override
    public Customer getByUser(User user) {
        logger.debug("從資料庫查詢客戶 - userId: {}, username: {}", user.getId(), user.getUsername());
        Query<Customer> query = getCurrentSession().createQuery(
            "FROM Customer c JOIN FETCH c.user WHERE c.user = :user", Customer.class);
        query.setParameter("user", user);
        Customer customer = query.uniqueResult();
        logger.debug("客戶查詢結果 - userId: {}, found: {}", user.getId(), customer != null);
        return customer;
    }

    /**
     * 新增或更新客戶
     * 
     * @param customer 客戶物件
     */
    @Override
    public void save(Customer customer) {
        logger.info("開始儲存客戶到資料庫 - customerId: {}, fullName: {}", 
                   customer.getId(), customer.getFullName());
        getCurrentSession().saveOrUpdate(customer);
        logger.info("客戶儲存成功 - customerId: {}, fullName: {}", customer.getId(), customer.getFullName());
    }

    /**
     * 依 ID 刪除客戶
     * 
     * @param id 客戶 ID
     */
    @Override
    public void delete(Long id) {
        logger.info("開始從資料庫刪除客戶 - customerId: {}", id);
        Customer customer = getCurrentSession().get(Customer.class, id);
        if (customer != null) {
            getCurrentSession().delete(customer);
            logger.info("客戶刪除成功 - customerId: {}", id);
        } else {
            logger.warn("要刪除的客戶不存在 - customerId: {}", id);
        }
    }
}
