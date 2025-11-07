-- ============================================
-- 驗證 admin 帳號並測試 BCrypt 密碼
-- ============================================

-- 1. 確認帳號資料
SELECT user_id, username, email, role, is_enabled, 
       LENGTH(password) AS password_length,
       SUBSTRING(password, 1, 7) AS password_prefix
FROM users 
WHERE username = 'admin';

-- 預期結果：
-- password_length 應該是 60
-- password_prefix 應該是 '$2a$10$'

-- ============================================
-- 如果 BCrypt 密碼值不正確，使用以下 SQL 更新
-- ============================================

-- 注意：這個 BCrypt 值是密碼 "admin123" 的加密結果
-- 如果這個值不正確，請使用 PasswordGenerator.java 重新生成

UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE username = 'admin';

-- ============================================
-- 確保帳號已啟用
-- ============================================

UPDATE users 
SET is_enabled = b'1' 
WHERE username = 'admin';

-- ============================================
-- 驗證更新後的帳號
-- ============================================

SELECT user_id, username, email, role, 
       CASE WHEN is_enabled = b'1' THEN '啟用' ELSE '停用' END AS status,
       created_at
FROM users 
WHERE username = 'admin';

