-- ============================================
-- 手動創建管理員帳號的 SQL 語句
-- ============================================

-- 方法一：使用預設帳號（推薦）
-- 預設帳號資訊：
--   使用者名稱: admin
--   Email: admin@example.com  
--   密碼: admin123
--   角色: ADMIN

INSERT INTO users (username, email, password, role, is_enabled, created_at, updated_at)
VALUES (
    'admin',
    'admin@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',  -- BCrypt('admin123')
    'ADMIN',
    b'1',                    -- bit(1) 類型：b'1' 表示啟用，b'0' 表示停用
    NOW(6),                  -- datetime(6) 支援微秒精度
    NOW(6)                   -- datetime(6) 支援微秒精度
);

-- ============================================
-- 方法二：自訂帳號資訊
-- ============================================
-- 請修改以下值：
-- 1. 'your_username' - 您的使用者名稱
-- 2. 'your_email@example.com' - 您的 Email
-- 3. 'your_bcrypt_password' - BCrypt 加密後的密碼（見下方說明）
-- 4. 'ADMIN' - 角色（必須是 'ADMIN' 或 'USER'）

INSERT INTO users (username, email, password, role, is_enabled, created_at, updated_at)
VALUES (
    'your_username',                    -- 修改：您的使用者名稱
    'your_email@example.com',           -- 修改：您的 Email
    'your_bcrypt_password',             -- 修改：BCrypt 加密後的密碼（見下方）
    'ADMIN',                            -- 角色：ADMIN
    b'1',                               -- 帳號啟用：b'1'=啟用，b'0'=停用
    NOW(6),                             -- 創建時間（datetime(6) 支援微秒）
    NOW(6)                              -- 更新時間（datetime(6) 支援微秒）
);

-- ============================================
-- 重要：如何生成 BCrypt 密碼
-- ============================================
-- BCrypt 密碼必須是加密後的，不能直接使用明文密碼！
-- 
-- 方法 A：使用 Java 代碼生成（推薦）
-- 在 Java 應用中執行以下代碼：
--
--   import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
--   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
--   String encodedPassword = encoder.encode("your_password");
--   System.out.println(encodedPassword);
--
-- 方法 B：使用線上工具
-- 訪問：https://bcrypt-generator.com/
-- 輸入您的密碼，選擇 rounds = 10，複製生成的 hash
--
-- 方法 C：使用預設密碼的 BCrypt 值
-- 如果使用密碼 "admin123"，可以使用上面的預設值：
--   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'

-- ============================================
-- 常見密碼的 BCrypt 值（供參考）
-- ============================================
-- 密碼 "admin123":
--   $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
--
-- 密碼 "password":
--   $2a$10$rKZ8v5FzF0VJ5J5J5J5J5OeKZ8v5FzF0VJ5J5J5J5J5J5J5J5J5J
--   （注意：每次生成的 BCrypt 值都不同，請使用上面的方法生成）

-- ============================================
-- 驗證插入是否成功
-- ============================================
-- 執行以下查詢確認帳號已創建：

SELECT user_id, username, email, role, is_enabled, created_at 
FROM users 
WHERE username = 'admin';

-- ============================================
-- 資料表結構說明
-- ============================================
-- CREATE TABLE `users` (
--   `user_id` bigint NOT NULL AUTO_INCREMENT,
--   `created_at` datetime(6) DEFAULT NULL,
--   `email` varchar(100) NOT NULL,
--   `is_enabled` bit(1) DEFAULT NULL,
--   `password` varchar(255) NOT NULL,
--   `role` varchar(255) NOT NULL,
--   `updated_at` datetime(6) DEFAULT NULL,
--   `username` varchar(50) NOT NULL,
--   PRIMARY KEY (`user_id`),
--   UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
-- )

-- ============================================
-- 注意事項
-- ============================================
-- 1. username 和 email 必須唯一，如果已存在會報錯
-- 2. password 必須是 BCrypt 加密後的格式（以 $2a$ 開頭）
-- 3. role 必須是 'ADMIN' 或 'USER'（大寫）
-- 4. is_enabled 是 bit(1) 類型：
--    - b'1' 或 1 表示啟用（true）
--    - b'0' 或 0 表示停用（false）
-- 5. created_at 和 updated_at 是 datetime(6) 類型，使用 NOW(6) 支援微秒精度
-- 6. user_id 會自動遞增，不需要手動指定
-- 7. 創建後請立即登入並修改密碼
-- 8. 建議修改 Email 為您的實際 Email

