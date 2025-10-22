package com.example.demo.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;

@Repository
@Transactional
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public User save(User user) {
        // 儲存新使用者到資料庫，用於註冊新用戶
        // Hibernate 會自動生成 ID 並設定到 user 物件中
        getCurrentSession().save(user);
        return user;
    }

    @Override
    public User getById(Long id) {
        // 根據使用者 ID 取得使用者，用於管理功能或個人資料頁面
        return getCurrentSession().get(User.class, id);
    }

    @Override
    public User getByUsername(String username) {
        // 根據使用者名稱取得使用者，這是登入時最重要的方法
        // Spring Security 會使用這個方法來驗證使用者身份
        Query<User> query = getCurrentSession().createQuery(
            "FROM User WHERE username = :username", User.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }

    @Override
    public User getByEmail(String email) {
        // 根據電子郵件取得使用者，用於忘記密碼功能或 email 登入
        Query<User> query = getCurrentSession().createQuery(
            "FROM User WHERE email = :email", User.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }

    @Override
    public List<User> getAll() {
        // 取得所有使用者，用於管理員查看所有註冊用戶
        // 按照註冊時間倒序排列（最新的在前面）
        Query<User> query = getCurrentSession().createQuery(
            "FROM User ORDER BY createdAt DESC", User.class);
        return query.getResultList();
    }

    @Override
    public User update(User user) {
        // 更新使用者資訊，用於修改個人資料或管理員更新用戶資料
        // 會自動觸發 @PreUpdate 更新 updatedAt 時間戳記
        getCurrentSession().update(user);
        return user;
    }

    @Override
    public void deleteById(Long id) {
        // 根據 ID 刪除使用者，用於管理員刪除用戶或用戶註銷帳號
        // 先查找使用者，如果存在才執行刪除操作
        User user = getById(id);
        if (user != null) {
            getCurrentSession().delete(user);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        // 檢查使用者名稱是否已存在，用於註冊時的驗證
        // 避免重複的使用者名稱，確保每個用戶名都是唯一的
        // Query<Long> query = getCurrentSession().createQuery(
        //     "SELECT COUNT(*) FROM User WHERE username = :username", Long.class);
        // query.setParameter("username", username);
        // return query.uniqueResult() > 0;
        Query<Integer> query = getCurrentSession().createQuery(
            "SELECT 1 FROM User WHERE username = :username", Integer.class);
        query.setParameter("username", username);
        query.setMaxResults(1); // 最多只查一筆即可
        return query.uniqueResultOptional().isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        // 檢查電子郵件是否已存在，用於註冊時的驗證
        // 避免重複的電子郵件，確保每個 email 都是唯一的
        // Query<Long> query = getCurrentSession().createQuery(
        //     "SELECT COUNT(*) FROM User WHERE email = :email", Long.class);
        // query.setParameter("email", email);
        // return query.uniqueResult() > 0;
        Query<Integer> query = getCurrentSession().createQuery(
            "SELECT 1 FROM User WHERE email = :email", Integer.class);
        query.setParameter("email", email);
        query.setMaxResults(1); // 最多只查一筆即可
        return query.uniqueResultOptional().isPresent();
    }
}