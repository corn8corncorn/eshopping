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

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;

/**
 * 用戶資料存取層實作類別
 * 負責與資料庫進行用戶相關的資料操作
 */
@Repository
@Transactional
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

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
     * 儲存新使用者到資料庫
     * Hibernate 會自動生成 ID 並設定到 user 物件中
     * 
     * @param user 要儲存的使用者物件
     * @return 儲存後的使用者物件（包含自動生成的 ID）
     */
    @Override
    public User save(User user) {
        logger.info("開始儲存使用者到資料庫 - username: {}", user.getUsername());
        getCurrentSession().save(user);
        logger.info("使用者儲存成功 - userId: {}, username: {}", user.getId(), user.getUsername());
        return user;
    }

    /**
     * 根據使用者 ID 取得使用者
     * 
     * @param id 使用者 ID
     * @return 使用者物件，如果不存在則返回null
     */
    @Override
    public User getById(Long id) {
        logger.debug("從資料庫取得使用者 - userId: {}", id);
        User user = getCurrentSession().get(User.class, id);
        logger.debug("使用者查詢結果 - userId: {}, found: {}", id, user != null);
        return user;
    }

    /**
     * 根據使用者名稱取得使用者
     * Spring Security 會使用這個方法來驗證使用者身份
     * 
     * @param username 使用者名稱
     * @return 使用者物件，如果不存在則返回null
     */
    @Override
    public User getByUsername(String username) {
        logger.debug("從資料庫查詢使用者 - username: {}", username);
        Query<User> query = getCurrentSession().createQuery(
            "FROM User WHERE username = :username", User.class);
        query.setParameter("username", username);
        User user = query.uniqueResult();
        logger.debug("使用者查詢結果 - username: {}, found: {}", username, user != null);
        return user;
    }

    /**
     * 根據電子郵件取得使用者
     * 用於忘記密碼功能或 email 登入
     * 
     * @param email 電子郵件
     * @return 使用者物件，如果不存在則返回null
     */
    @Override
    public User getByEmail(String email) {
        logger.debug("從資料庫查詢使用者 - email: {}", email);
        Query<User> query = getCurrentSession().createQuery(
            "FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);
        User user = query.uniqueResult();
        logger.debug("使用者查詢結果 - email: {}, found: {}", email, user != null);
        return user;
    }

    /**
     * 取得所有使用者
     * 按照註冊時間倒序排列（最新的在前面）
     * 
     * @return 所有使用者的列表
     */
    @Override
    public List<User> getAll() {
        logger.debug("從資料庫查詢所有使用者");
        Query<User> query = getCurrentSession().createQuery(
            "FROM User ORDER BY createdAt DESC", User.class);
        List<User> users = query.getResultList();
        logger.info("成功查詢到 {} 個使用者", users.size());
        return users;
    }

    /**
     * 更新使用者資訊
     * 會自動觸發 @PreUpdate 更新 updatedAt 時間戳記
     * 
     * @param user 要更新的使用者物件
     * @return 更新後的使用者物件
     */
    @Override
    public User update(User user) {
        logger.info("開始更新使用者 - userId: {}, username: {}", user.getId(), user.getUsername());
        getCurrentSession().update(user);
        logger.info("使用者更新成功 - userId: {}", user.getId());
        return user;
    }

    /**
     * 根據 ID 刪除使用者
     * 先查找使用者，如果存在才執行刪除操作
     * 
     * @param id 要刪除的使用者 ID
     */
    @Override
    public void deleteById(Long id) {
        logger.info("開始從資料庫刪除使用者 - userId: {}", id);
        User user = getById(id);
        if (user != null) {
            getCurrentSession().delete(user);
            logger.info("使用者刪除成功 - userId: {}", id);
        } else {
            logger.warn("要刪除的使用者不存在 - userId: {}", id);
        }
    }

    /**
     * 檢查使用者名稱是否已存在
     * 避免重複的使用者名稱，確保每個用戶名都是唯一的
     * 
     * @param username 使用者名稱
     * @return 如果存在返回true，否則返回false
     */
    @Override
    public boolean existsByUsername(String username) {
        logger.debug("檢查使用者名稱是否存在 - username: {}", username);
        Query<Integer> query = getCurrentSession().createQuery(
            "SELECT 1 FROM User WHERE username = :username", Integer.class);
        query.setParameter("username", username);
        query.setMaxResults(1); // 最多只查一筆即可
        boolean exists = query.uniqueResultOptional().isPresent();
        logger.debug("使用者名稱檢查結果 - username: {}, exists: {}", username, exists);
        return exists;
    }

    /**
     * 檢查電子郵件是否已存在
     * 避免重複的電子郵件，確保每個 email 都是唯一的
     * 
     * @param email 電子郵件
     * @return 如果存在返回true，否則返回false
     */
    @Override
    public boolean existsByEmail(String email) {
        logger.debug("檢查電子郵件是否存在 - email: {}", email);
        Query<Integer> query = getCurrentSession().createQuery(
            "SELECT 1 FROM User WHERE email = :email", Integer.class);
        query.setParameter("email", email);
        query.setMaxResults(1); // 最多只查一筆即可
        boolean exists = query.uniqueResultOptional().isPresent();
        logger.debug("電子郵件檢查結果 - email: {}, exists: {}", email, exists);
        return exists;
    }
}
