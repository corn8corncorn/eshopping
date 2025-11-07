-- ============================================
-- 檢查 admin 帳號的角色設定
-- ============================================

-- 1. 檢查 admin 帳號的完整資訊
SELECT user_id, username, email, role, is_enabled 
FROM users 
WHERE username = 'admin';

-- 2. 確認角色是 ADMIN（必須是大寫）
-- 如果 role 不是 'ADMIN'，執行以下 SQL 更新：
UPDATE users 
SET role = 'ADMIN' 
WHERE username = 'admin';

-- 3. 確認帳號已啟用
UPDATE users 
SET is_enabled = b'1' 
WHERE username = 'admin';

-- 4. 驗證更新後的結果
SELECT user_id, username, email, role, is_enabled,
       CASE WHEN role = 'ADMIN' THEN '✓ 角色正確' ELSE '✗ 角色錯誤' END AS role_check,
       CASE WHEN is_enabled = b'1' THEN '✓ 帳號啟用' ELSE '✗ 帳號停用' END AS status_check
FROM users 
WHERE username = 'admin';

