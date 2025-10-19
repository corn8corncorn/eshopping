# Spring Security 登入功能說明

## 功能概述
本專案已成功整合Spring Security，提供完整的用戶認證功能，包括：
- 用戶註冊
- 用戶登入
- 用戶登出
- 基於角色的權限控制

## 新增的功能

### 1. 依賴項目
已在 `pom.xml` 中添加Spring Security相關依賴：
- spring-security-web
- spring-security-config  
- spring-security-taglibs

### 2. 數據庫模型更新
`User` 模型已更新，新增字段：
- `password`: 用戶密碼（使用BCrypt加密）
- `role`: 用戶角色（預設為"USER"）

### 3. 安全配置
- `SecurityConfig.java`: Spring Security主要配置
- `SecurityWebApplicationInitializer.java`: Security Filter初始化
- `CustomUserDetailsService.java`: 自定義用戶認證服務

### 4. 控制器
- `AuthController.java`: 處理登入、註冊相關請求
- `HomeController.java`: 更新以支援登入狀態顯示

### 5. 頁面
- `login.html`: 登入頁面
- `register.html`: 註冊頁面  
- `home.html`: 更新首頁以顯示登入狀態

## 使用方式

### 訪問頁面
- 首頁: `http://localhost:8080/eshop/home`
- 登入: `http://localhost:8080/eshop/login`
- 註冊: `http://localhost:8080/eshop/register`

### 註冊新用戶
1. 訪問註冊頁面
2. 填寫姓名、Email、密碼
3. 確認密碼
4. 點擊註冊按鈕

### 登入
1. 訪問登入頁面
2. 輸入Email和密碼
3. 點擊登入按鈕

### 登出
1. 在已登入狀態下，點擊首頁的"登出"按鈕

## 權限控制
- 公開頁面: `/`, `/home`, `/register`, `/login`
- 需要認證: 其他所有頁面
- 管理員頁面: `/admin/**` (需要ADMIN角色)

## 數據庫
系統會自動創建 `users` 表，包含以下字段：
- `id`: 主鍵
- `name`: 用戶姓名
- `email`: 用戶Email（唯一）
- `password`: 加密後的密碼
- `role`: 用戶角色

## 注意事項
1. 密碼使用BCrypt加密存儲
2. 預設關閉CSRF保護（開發環境）
3. 用戶角色預設為"USER"
4. 支援中文界面
