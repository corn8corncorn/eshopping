-- ============================================
-- 排查 admin 登入失敗問題的 SQL 查詢
-- ============================================

-- 1. 檢查 admin 帳號是否存在
SELECT user_id, username, email, role, is_enabled, created_at 
FROM users 
WHERE username = 'admin';

-- 2. 檢查帳號是否啟用（is_enabled 應該是 1）
SELECT user_id, username, is_enabled, 
       CASE WHEN is_enabled = b'1' THEN '啟用' ELSE '停用' END AS status
FROM users 
WHERE username = 'admin';

-- 3. 檢查所有管理員帳號
SELECT user_id, username, email, role, is_enabled 
FROM users 
WHERE role = 'ADMIN';

-- ============================================
-- 如果帳號不存在，執行以下 SQL 創建
-- ============================================

-- 方法一：使用預設密碼 admin123
INSERT INTO users (username, email, password, role, is_enabled, created_at, updated_at)
VALUES (
    'admin',
    'admin@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ADMIN',
    b'1',
    NOW(6),
    NOW(6)
);

-- ============================================
-- 如果帳號存在但登入失敗，嘗試以下解決方案
-- ============================================

-- 方案 A：確保帳號已啟用
UPDATE users 
SET is_enabled = b'1' 
WHERE username = 'admin';

-- 方案 B：重置密碼為 admin123（使用新的 BCrypt 值）
-- 注意：如果上面的 BCrypt 值不正確，需要重新生成
-- 請使用 PasswordGenerator.java 生成新的 BCrypt 值

-- 方案 C：刪除舊帳號並重新創建
DELETE FROM users WHERE username = 'admin';
INSERT INTO users (username, email, password, role, is_enabled, created_at, updated_at)
VALUES (
    'admin',
    'admin@example.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ADMIN',
    b'1',
    NOW(6),
    NOW(6)
);

-- ============================================
-- 驗證密碼 BCrypt 值是否正確
-- ============================================
-- 如果登入仍然失敗，可能是 BCrypt 密碼值不正確
-- 請使用 PasswordGenerator.java 重新生成密碼 "admin123" 的 BCrypt 值
-- 然後執行以下 SQL 更新密碼：

-- UPDATE users 
-- SET password = '新的BCrypt值' 
-- WHERE username = 'admin';

