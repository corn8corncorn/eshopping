## Git Commit Message

### Type: feat
### Scope: cart-and-order
### Subject: 新增購物車和訂單管理功能

### Body:
實作完整的購物車和訂單管理系統，包括：

**購物車功能 (CartController):**
- 顯示購物車頁面
- 添加商品到購物車
- 更新購物車商品數量（包含庫存檢查）
- 從購物車移除商品
- 清空購物車

**訂單管理功能 (OrderController):**
- 訂單列表頁面（管理員和用戶）
- 我的訂單列表（用戶專屬）
- 訂單詳情頁面
- 結帳功能（從購物車建立訂單）
- 取消訂單功能（用戶）
- 更新訂單狀態（管理員）
- 更新付款狀態（管理員）

**View 頁面:**
- cart.html - 購物車頁面，支援商品數量更新和移除
- checkout.html - 結帳頁面，包含收件人資訊表單和訂單摘要
- orders.html - 訂單列表頁面，支援管理員和用戶視圖
- order-detail.html - 訂單詳情頁面，包含訂單項目、總計和管理員操作

**安全設定更新:**
- 更新 SecurityConfig，添加購物車和訂單路由的權限控制
- 購物車路由 (/cart/**) 需要 USER 角色
- 訂單路由 (/orders/**) 需要 USER 角色
- 管理員特定訂單操作需要 ADMIN 角色

**功能特點:**
- 完整的庫存檢查機制
- 購物車到訂單的自動轉換
- 訂單建立時自動減少庫存
- 訂單取消時自動恢復庫存
- 詳細的日誌記錄（使用 Logback）
- 完整的錯誤處理和用戶提示

### Breaking Changes: 無

### Related Issues: N/A

---

## 簡化版本（單行）

```
feat(cart-and-order): 新增購物車和訂單管理功能

實作完整的購物車和訂單管理系統，包括 CartController（購物車CRUD）、
OrderController（訂單管理、結帳、取消）、4個View頁面（cart、checkout、
orders、order-detail）和安全設定更新。功能包含庫存檢查、自動轉換購物車
為訂單、完整的權限控制和錯誤處理。
```

---

## 使用方式

```bash
git add .
git commit -F COMMIT_MESSAGE.md
```

或者直接使用簡化版本：

```bash
git add .
git commit -m "feat(cart-and-order): 新增購物車和訂單管理功能

實作完整的購物車和訂單管理系統，包括 CartController（購物車CRUD）、
OrderController（訂單管理、結帳、取消）、4個View頁面（cart、checkout、
orders、order-detail）和安全設定更新。功能包含庫存檢查、自動轉換購物車
為訂單、完整的權限控制和錯誤處理。"
```

