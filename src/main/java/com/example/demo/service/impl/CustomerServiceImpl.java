package com.example.demo.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.service.CustomerService;

/**
 * 客戶服務層實作類別
 * 負責處理客戶相關的業務邏輯
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerDAO customerDAO;

    /**
     * 取得所有客戶
     * 
     * @return 所有客戶的列表
     */
    @Override
    public List<Customer> getAll() {
        logger.debug("開始取得所有客戶");
        List<Customer> customers = customerDAO.getAll();
        logger.info("成功取得 {} 個客戶", customers.size());
        return customers;
    }

    /**
     * 依 ID 取得客戶
     * 
     * @param id 客戶 ID
     * @return 客戶物件，如果不存在則返回null
     */
    @Override
    public Customer getById(Long id) {
        logger.debug("開始取得客戶 - customerId: {}", id);
        Customer customer = customerDAO.getById(id);
        logger.debug("客戶取得結果 - customerId: {}, found: {}", id, customer != null);
        return customer;
    }

    /**
     * 依 User 取得客戶
     * 
     * @param user 使用者物件
     * @return 客戶物件，如果不存在則返回null
     */
    @Override
    public Customer getByUser(User user) {
        logger.debug("開始取得客戶 - userId: {}, username: {}", user.getId(), user.getUsername());
        Customer customer = customerDAO.getByUser(user);
        logger.debug("客戶取得結果 - userId: {}, found: {}", user.getId(), customer != null);
        return customer;
    }

    /**
     * 新增客戶
     * 
     * @param customer 客戶物件
     */
    @Override
    public void save(Customer customer) {
        logger.info("開始儲存新客戶 - fullName: {}", customer.getFullName());
        customerDAO.save(customer);
        logger.info("客戶儲存成功 - customerId: {}, fullName: {}", customer.getId(), customer.getFullName());
    }

    /**
     * 更新客戶
     * 根據 ID 查找現有客戶並更新資料
     * 
     * @param id 客戶 ID
     * @param customer 包含新客戶資料的物件
     */
    @Override
    public void update(Long id, Customer customer) {
        logger.info("開始更新客戶 - customerId: {}", id);
        Customer existingCustomer = customerDAO.getById(id);
        if (existingCustomer != null) {
            logger.debug("找到現有客戶 - fullName: {}", existingCustomer.getFullName());
            
            // 更新客戶資料
            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setPhone(customer.getPhone());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setCity(customer.getCity());
            existingCustomer.setPostalCode(customer.getPostalCode());
            existingCustomer.setCountry(customer.getCountry());
            
            customerDAO.save(existingCustomer);
            logger.info("客戶更新成功 - customerId: {}", id);
        } else {
            logger.warn("客戶不存在 - customerId: {}", id);
        }
    }

    /**
     * 刪除客戶
     * 
     * @param id 客戶 ID
     */
    @Override
    public void delete(Long id) {
        logger.info("開始刪除客戶 - customerId: {}", id);
        customerDAO.delete(id);
        logger.info("客戶刪除成功 - customerId: {}", id);
    }

    /**
     * 為新註冊用戶建立客戶資料
     * 
     * @param user 使用者物件
     * @param fullName 客戶全名
     * @return 創建成功的客戶物件
     */
    @Override
    public Customer createCustomerForUser(User user, String fullName) {
        logger.info("開始為使用者建立客戶資料 - userId: {}, username: {}, fullName: {}", 
                   user.getId(), user.getUsername(), fullName);
        
        Customer customer = new Customer(user, fullName);
        customerDAO.save(customer);
        
        logger.info("客戶資料建立成功 - customerId: {}, userId: {}", customer.getId(), user.getId());
        return customer;
    }
}
