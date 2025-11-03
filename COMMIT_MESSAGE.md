## Git Commit Message

### Type: feat
### Scope: customer-edit
### Subject: 擴充客戶資料編輯功能，支援修改密碼並顯示使用者資訊

### Body:
擴充客戶資料編輯頁面功能，添加使用者名稱顯示（只讀）、電子郵件顯示（只讀），以及支援修改密碼功能。同時修正電話欄位類型問題。

**主要變更:**

1. **edit-customer.html - 編輯客戶資料頁面**
   - 添加使用者名稱顯示欄位
     - 在表單最上方添加「使用者名稱」欄位
     - 設定為只讀（`readonly`），使用灰色背景樣式
     - 使用 `th:value` 顯示當前使用者名稱，不可修改
   - 添加電子郵件顯示欄位
     - 將 email 欄位改為只讀顯示（`readonly`）
     - 使用灰色背景樣式，表示不可編輯
     - 使用 `th:value` 顯示當前電子郵件
   - 添加密碼修改欄位
     - 添加「新密碼」輸入欄位（`type="password"`）
     - 可選欄位，留空則不修改密碼
     - 添加提示文字：「※ 若不修改密碼，請留空」
   - 修正電話欄位類型
     - 將電話欄位從 `type="tel"` 改為 `type="text"`
     - 避免在某些瀏覽器中顯示為密碼類型
   - 添加 User ID 隱藏欄位
     - 確保 User 關聯正確傳遞到後端

2. **CustomerServiceImpl.java - 客戶服務層**
   - 添加 UserService 依賴注入
     - 用於處理 User 資料的更新操作
   - 擴充 `update()` 方法功能
     - 原本只更新 Customer 的 `fullName` 和 `phone`
     - 現在支援同時更新 User 的 `password`（如果提供）
     - Email 更新功能已移除（email 改為只讀，不可修改）
   - 密碼處理邏輯
     - 只有在提供新密碼時才更新
     - 自動使用 `UserService.encodePassword()` 進行加密
     - 保存更新後的 User 到資料庫
   - 添加異常處理
     - 如果客戶不存在，拋出 `IllegalArgumentException`

3. **CustomerController.java - 客戶管理控制器**
   - 擴充 `updateCustomer()` 方法
     - 添加 `RedirectAttributes` 參數，支援顯示成功/錯誤訊息
     - 處理空密碼：如果 password 為空，設為 null，避免更新密碼
     - 添加異常處理（`IllegalArgumentException` 和其他異常）
     - 更新成功後重定向到 `/customers/profile` 並顯示成功訊息
     - 更新失敗時重定向回 `/customers/edit` 並顯示錯誤訊息
   - 改進錯誤處理
     - 區分不同類型的錯誤（參數錯誤、系統錯誤）
     - 提供清晰的錯誤訊息給用戶

**問題修復:**

- **問題**: 電話欄位在某些情況下顯示為密碼類型
  - **原因**: 使用 `type="tel"` 可能導致某些瀏覽器或自動填充功能誤判
  - **解決**: 改為 `type="text"`，確保正確顯示為文字輸入框

**功能改進:**

- **可編輯欄位**:
  - `fullName`（姓名）- 必填
  - `phone`（電話）- 可選
  - `password`（新密碼）- 可選，留空則不修改

- **只讀顯示欄位**:
  - `username`（使用者名稱）- 顯示在表單最上方，不可修改
  - `email`（電子郵件）- 顯示當前 email，不可修改

**安全特性:**

- 密碼自動加密：新密碼會使用 BCrypt 加密後存儲
- 密碼可選更新：留空則不修改，保持原有密碼
- Email 不可修改：確保帳號安全性，email 只能通過管理員修改

**技術細節:**

- 使用 `readonly` 屬性防止用戶修改使用者名稱和電子郵件
- 使用 `th:value` 而非 `th:field` 顯示只讀欄位，避免表單綁定
- 密碼欄位使用 `th:field="*{user.password}"` 綁定，但後端會檢查是否為空
- 後端使用 `UserService.encodePassword()` 確保密碼正確加密
- 使用 `RedirectAttributes` 傳遞 flash 訊息，提升用戶體驗

**UI/UX 改進:**

- 使用者名稱和電子郵件欄位使用灰色背景，清楚表示不可編輯
- 表單欄位順序：使用者名稱 → 姓名 → 電話 → 電子郵件 → 新密碼
- 提供清晰的提示文字，說明密碼欄位的使用方式
- 統一的錯誤處理和成功訊息顯示
