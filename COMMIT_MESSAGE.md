## Git Commit Message

### Type: feat
### Scope: shop-frontend
### Subject: 新增前台商品展示系統

### Body:
實作完整的前台商品展示系統，補齊用戶購物流程的關鍵功能。

**ShopController（前台商品展示控制器）:**
- GET /shop - 商品展示頁面（支援分類篩選和搜尋）
- GET /shop/product/{id} - 商品詳情頁
- POST /shop/product/{id}/add-to-cart - 從商品詳情頁加入購物車
- GET /shop/category/{category} - 分類商品頁面（重定向到 shop 頁面）

**View 頁面:**
- shop.html - 前台商品展示頁面
  - 響應式網格布局設計
  - 商品卡片展示（圖片、名稱、價格、庫存狀態）
  - 分類篩選功能
  - 關鍵字搜尋功能
  - 庫存狀態顯示（充足/不足/缺貨）
  - 加入購物車按鈕
  - 查看詳情連結

- product-detail.html - 商品詳情頁面
  - 商品詳細資訊展示
  - 商品圖片顯示
  - 庫存資訊和狀態顯示
  - 數量選擇器（限制在庫存範圍內）
  - 加入購物車功能
  - 立即購買功能
  - 麵包屑導航

**安全設定和導航更新:**
- 更新 SecurityConfig，允許所有人訪問 /shop/**（前台商品展示不需要登入）
- 更新首頁導航選單，加入「商品展示」、「購物車」、「我的訂單」連結
- 管理員區域增加「訂單管理」連結

**功能特點:**
- 只顯示上架中的商品（ACTIVE 狀態），自動過濾下架和缺貨商品
- 支援根據商品類型（type）進行分類篩選
- 支援商品名稱和描述的關鍵字搜尋
- 庫存狀態即時顯示和檢查
- 響應式設計（RWD），支援手機和平板瀏覽
- 與現有購物車系統完美整合

**購物流程補齊:**
此功能補齊了完整的用戶購物流程：
瀏覽商品 → 查看詳情 → 加入購物車 → 結帳 → 查看訂單

### Breaking Changes: 無

### Related Issues: 完成 MISSING_FEATURES.md 中的前台商品展示系統需求

---

## 簡化版本（單行）

```
feat(shop-frontend): 新增前台商品展示系統

實作完整的前台商品展示系統，包括 ShopController、shop.html（商品列表）、
product-detail.html（商品詳情）和相關功能。支援分類篩選、關鍵字搜尋、
庫存狀態顯示，並與購物車系統整合。更新安全設定允許所有人訪問商品展示
頁面，更新首頁導航選單。補齊完整的用戶購物流程。
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
git commit -m "feat(shop-frontend): 新增前台商品展示系統

實作完整的前台商品展示系統，包括 ShopController、shop.html（商品列表）、
product-detail.html（商品詳情）和相關功能。支援分類篩選、關鍵字搜尋、
庫存狀態顯示，並與購物車系統整合。更新安全設定允許所有人訪問商品展示
頁面，更新首頁導航選單。補齊完整的用戶購物流程。"
```
