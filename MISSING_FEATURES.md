# eShopping 專案缺少的功能和頁面清單

## 📋 現有功能總覽

### ✅ 已完成的 Model
- User (用戶)
- Customer (客戶)
- Product (商品)
- Order (訂單)
- OrderItem (訂單項目)
- Cart (購物車)
- CartItem (購物車項目)

### ✅ 已完成的 Service & DAO
- UserService / UserDAO
- CustomerService / CustomerDAO
- ProductService / ProductDAO
- OrderService / OrderDAO
- OrderItemService / OrderItemDAO
- CartService / CartDAO
- CartItemService / CartItemDAO

### ✅ 已完成的 Controller
- AuthController (認證)
- UserController (用戶管理)
- CustomerController (客戶管理)
- ProductController (商品管理)
- HomeController (首頁)

### ✅ 已完成的 View 頁面
- login.html (登入)
- register.html (註冊)
- forgot-password.html (忘記密碼)
- reset-password.html (重設密碼)
- home.html (首頁)
- users.html (用戶列表)
- add-user.html (新增用戶)
- edit-user.html (編輯用戶)
- customers.html (客戶列表)
- customer-profile.html (客戶個人資料)
- edit-customer.html (編輯客戶)
- products.html (商品列表)
- add-product.html (新增商品)
- edit-product.html (編輯商品)

---

## ❌ 缺少的 Controller

### 1. OrderController (訂單管理控制器)
**建議功能：**
- `GET /orders` - 訂單列表頁（管理員/用戶）
- `GET /orders/my` - 我的訂單列表（當前用戶）
- `GET /orders/{id}` - 訂單詳情頁
- `GET /orders/create` - 建立訂單頁面（從購物車）
- `POST /orders/create` - 提交訂單
- `POST /orders/{id}/cancel` - 取消訂單
- `POST /orders/{id}/update-status` - 更新訂單狀態（管理員）
- `POST /orders/{id}/update-payment-status` - 更新付款狀態（管理員）

### 2. CartController (購物車管理控制器)
**建議功能：**
- `GET /cart` - 購物車頁面（顯示購物車內容）
- `POST /cart/add` - 添加商品到購物車
- `POST /cart/update/{productId}` - 更新購物車商品數量
- `POST /cart/remove/{productId}` - 從購物車移除商品
- `POST /cart/clear` - 清空購物車
- `GET /cart/checkout` - 結帳頁面（從購物車建立訂單）
- `POST /cart/checkout` - 提交結帳

### 3. ProductViewController (商品展示控制器 - 前台)
**建議功能：**
- `GET /shop` - 商品展示頁（前台瀏覽商品）
- `GET /shop/product/{id}` - 商品詳情頁
- `GET /shop/category/{category}` - 分類商品列表
- `POST /shop/product/{id}/add-to-cart` - 從商品詳情頁加入購物車

### 4. OrderController 補充功能
**管理員功能：**
- `GET /orders/status/{status}` - 根據狀態篩選訂單
- `GET /orders/customer/{customerId}` - 查看特定客戶的訂單
- `GET /orders/statistics` - 訂單統計頁面

**用戶功能：**
- `GET /orders/my/status/{status}` - 我的訂單依狀態篩選
- `GET /orders/my/{id}` - 我的訂單詳情

---

## ❌ 缺少的 View 頁面

### 訂單相關頁面
1. **orders.html** - 訂單列表頁（管理員/用戶）
   - 顯示所有訂單或當前用戶的訂單
   - 支援狀態篩選
   - 訂單編號、日期、金額、狀態顯示

2. **order-detail.html** - 訂單詳情頁
   - 顯示訂單完整資訊
   - 訂單項目列表
   - 收件人資訊
   - 付款資訊
   - 訂單狀態和操作按鈕（取消、查看物流等）

3. **create-order.html** - 建立訂單頁面
   - 從購物車轉換為訂單
   - 填寫收件資訊
   - 選擇付款方式
   - 確認訂單內容

4. **order-confirmation.html** - 訂單確認頁
   - 訂單建立成功後的確認頁
   - 顯示訂單編號
   - 提供後續操作連結

### 購物車相關頁面
5. **cart.html** - 購物車頁面
   - 顯示購物車中的所有商品
   - 顯示商品名稱、價格、數量
   - 支援修改數量、移除商品
   - 顯示總金額
   - 結帳按鈕

6. **checkout.html** - 結帳頁面
   - 確認購物車內容
   - 填寫收件人資訊
   - 選擇付款方式
   - 確認訂單金額（含運費、折扣）
   - 提交訂單

### 前台商品展示頁面（注意：現有的 products.html 是後台管理頁）
7. **shop.html** - 商品展示頁（前台購物頁面）
   - 商品列表展示（網格/列表視圖切換）
   - 商品卡片顯示（圖片、名稱、價格、庫存狀態）
   - 分類篩選功能
   - 搜尋功能
   - 價格排序功能
   - 加入購物車按鈕
   - 查看詳情連結

8. **product-detail.html** - 商品詳情頁（前台）
   - 商品詳細資訊展示
   - 商品圖片放大/輪播
   - 商品完整描述
   - 庫存資訊顯示
   - 數量選擇器（帶庫存檢查）
   - 加入購物車按鈕
   - 立即購買按鈕（跳轉結帳）

9. **shop-category.html** - 分類商品頁
   - 根據商品類型（type）顯示商品
   - 分類瀏覽導航
   - 分類麵包屑導航

### 首頁增強
10. **home.html (增強)** - 首頁改進
    - 展示熱門商品
    - 商品分類導航
    - 推薦商品區塊
    - 最新商品區塊
    - 限時特價商品（可選）

### UI/UX 增強
11. **通用導航選單更新**
    - 購物車圖標（顯示商品數量）
    - 我的訂單連結
    - 分類導航選單
    - 搜尋欄（全局）

12. **購物車圖標組件**
    - 顯示購物車商品總數
    - 點擊顯示購物車快顯視窗（可選）
    - 或直接跳轉購物車頁面

---

## ❌ 缺少的功能模組

### 1. DTO (資料傳輸物件)
- **OrderDTO** - 訂單建立時的資料傳輸物件
- **CartDTO** - 購物車操作時的資料傳輸物件
- **CheckoutDTO** - 結帳時的資料傳輸物件

### 2. 表單驗證
- 訂單建立的表單驗證
- 收件資訊驗證
- 購物車數量驗證

### 3. 業務邏輯增強
- **庫存檢查** - 在添加到購物車和建立訂單時檢查庫存
- **價格同步** - 購物車轉訂單時的價格驗證
- **訂單狀態流程控制** - 狀態變更的業務規則
- **取消訂單的庫存恢復** - 取消訂單時恢復庫存
- **購物車到訂單轉換** - 從購物車建立訂單的邏輯
- **訂單編號生成規則** - 確保唯一性

### 4. 錯誤處理
- 庫存不足錯誤處理頁面
- 訂單建立失敗錯誤處理
- 購物車操作錯誤處理

### 5. 訊息提示
- 成功訊息（加入購物車成功、訂單建立成功）
- 錯誤訊息（庫存不足、操作失敗）
- Flash 訊息機制（RedirectAttributes）

### 6. 分頁和排序（可選但建議）
- 商品列表分頁
- 訂單列表分頁
- 商品排序（價格、名稱、日期）
- 商品篩選（分類、價格區間、庫存狀態）

### 7. AJAX 功能（可選但建議）
- 購物車添加商品（AJAX，不刷新頁面）
- 購物車數量更新（AJAX）
- 購物車商品移除（AJAX）
- 庫存即時檢查（AJAX）

---

## 📊 優先級建議

### 高優先級 (核心電商功能)
1. **CartController** + **cart.html** - 購物車功能
2. **OrderController** + **orders.html** + **order-detail.html** - 訂單查看功能
3. **create-order.html** + **checkout.html** - 結帳流程
4. **shop.html** + **product-detail.html** - 前台商品展示

### 中優先級 (體驗優化)
5. **shop-category.html** - 分類瀏覽
6. **order-confirmation.html** - 訂單確認頁
7. 庫存檢查和錯誤處理
8. 訊息提示功能

### 低優先級 (管理功能)
9. 訂單統計頁面
10. 訂單狀態管理（管理員）

---

## 🔧 技術建議

### Controller 層
- 使用 `@AuthenticationPrincipal` 取得當前用戶
- 使用 `@Valid` 進行表單驗證
- 使用 `RedirectAttributes` 傳遞成功/錯誤訊息
- 適當的異常處理

### View 層
- 使用 Thymeleaf 模板引擎
- 響應式設計（RWD）
- AJAX 支援（購物車操作、數量更新）
- 表單驗證（前端 + 後端）

### 安全考慮
- 購物車只能訪問自己的購物車
- 訂單只能查看自己的訂單（非管理員）
- 訂單建立需要登入驗證
- CSRF 保護（雖然目前關閉，但生產環境應啟用）

---

## 📝 總結

**缺少的主要功能：**
1. ❌ OrderController - 訂單管理前端
2. ❌ CartController - 購物車前端
3. ❌ ProductViewController - 前台商品展示
4. ❌ 8-10 個 HTML 頁面
5. ❌ DTO 物件（可選但建議）
6. ❌ 庫存檢查和業務邏輯增強

**建議開發順序：**
1. 先建立 CartController 和購物車頁面（核心功能）
2. 建立 OrderController 和訂單相關頁面（完成購買流程）
3. 建立前台商品展示頁面（用戶體驗）
4. 增強功能和優化（錯誤處理、訊息提示等）

