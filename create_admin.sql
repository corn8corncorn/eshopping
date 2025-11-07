-- 創建第一個管理員帳號的 SQL 腳本
-- 使用前請先修改以下資訊：
-- 1. username: 管理員使用者名稱
-- 2. email: 管理員 Email
-- 3. password: 管理員密碼（這裡是 BCrypt 加密後的密碼，預設密碼為 "admin123"）

-- 注意：BCrypt 加密後的密碼每次都不一樣，所以這裡提供的是預設密碼 "admin123" 的加密結果
-- 如果您想使用其他密碼，請使用以下 Java 代碼生成 BCrypt 密碼：
-- BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
-- String encodedPassword = encoder.encode("your_password");

-- 預設管理員帳號資訊：
-- 使用者名稱: admin
-- Email: admin@example.com
-- 密碼: admin123 (BCrypt 加密後)

-- 根據實際資料表結構創建管理員帳號
-- 資料表結構：
--   is_enabled: bit(1) - 使用 b'1' 表示啟用，b'0' 表示停用
--   created_at/updated_at: datetime(6) - 使用 NOW(6) 支援微秒精度

INSERT INTO users (username, email, password, role, is_enabled, created_at, updated_at)
VALUES (
    'admin',                                    -- 使用者名稱
    'admin@example.com',                        -- Email
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',  -- BCrypt 加密後的密碼 (admin123)
    'ADMIN',                                    -- 角色：ADMIN
    b'1',                                       -- 帳號啟用（bit(1) 類型）
    NOW(6),                                     -- 創建時間（datetime(6) 支援微秒）
    NOW(6)                                      -- 更新時間（datetime(6) 支援微秒）
)
ON DUPLICATE KEY UPDATE 
    username = username;  -- 如果已存在則不更新

-- 使用說明：
-- 1. 執行此 SQL 腳本後，可以使用以下帳號登入：
--    使用者名稱: admin
--    密碼: admin123
-- 
-- 2. 登入後請立即：
--    - 訪問 /users/edit/{admin_user_id} 修改密碼
--    - 修改 Email 為您的實際 Email
--
-- 3. 如果您想使用不同的使用者名稱、Email 或密碼，請修改上面的 VALUES 中的值
--    並使用 BCryptPasswordEncoder 重新生成密碼的 BCrypt 值

