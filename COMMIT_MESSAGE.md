## Git Commit Message

### Type: fix
### Scope: product-stock-quantity
### Subject: 修復商品庫存數量欄位缺失及更新問題

### Body:
添加商品庫存數量欄位到新增/編輯商品表單，並修復庫存數量無法正確更新到資料庫的問題。

**主要變更:**

1. **表單欄位添加**
   - **add-product.html**：
     - 在價格和描述欄位之間添加「庫存數量」欄位
     - 欄位屬性：`type="number"`, `min="0"`, `required`
     - 綁定到 `stockQuantity` 欄位
     - 包含錯誤訊息顯示區域
   - **edit-product.html**：
     - 同樣添加「庫存數量」欄位
     - 確保編輯時可以修改庫存數量

2. **後端模型驗證**
   - **Product.java**：
     - 為 `stockQuantity` 添加 `@NotNull` 驗證註解
     - 添加 `@Min(value = 0)` 驗證註解，確保不能為負數
     - 添加 `@DecimalMin` 和 `@NotNull` 到 `price` 欄位（補充之前的驗證）

3. **Service 層修復**
   - **ProductServiceImpl.java**：
     - 在 `updateProduct()` 方法中添加 `stockQuantity` 的更新邏輯
     - 添加詳細的調試日誌追蹤庫存數量更新過程
     - 確保 `stockQuantity` 不為 null 時才更新

4. **DAO 層優化**
   - **ProductDAOImpl.java**：
     - 將 `saveOrUpdate()` 改為使用 `merge()` 處理現有商品更新
     - 對於新商品使用 `save()`，對於現有商品使用 `merge()`
     - 添加 `flush()` 確保立即寫入資料庫
     - 增強日誌記錄，包含 `stockQuantity` 資訊

5. **Controller 優化**
   - **ProductController.java**：
     - 簡化日誌輸出，保留必要的追蹤資訊
     - 確保驗證錯誤正確處理

**問題修復:**

- **問題**: 新增商品後，所有商品都是缺貨狀態
  - **原因**: 表單缺少庫存數量欄位，預設值為 0，導致 `updateStatusBasedOnStock()` 自動設為 `OUT_OF_STOCK`
  - **解決**: 添加庫存數量欄位，允許用戶輸入庫存數量

- **問題**: 編輯商品數量後，沒有寫進資料庫
  - **原因**: 
    1. 表單缺少 `stockQuantity` 欄位
    2. Service 層的 `updateProduct()` 方法沒有更新 `stockQuantity`
    3. DAO 層使用 `saveOrUpdate()` 可能無法正確處理更新
  - **解決**: 
    1. 添加表單欄位
    2. 在 Service 層添加 `stockQuantity` 更新邏輯
    3. DAO 層改用 `merge()` 並添加 `flush()`

**技術細節:**

- 使用 Hibernate `merge()` 確保實體狀態正確合併
- 使用 `flush()` 強制立即同步到資料庫
- 添加驗證註解確保資料完整性
- 庫存數量更新時會自動觸發 `updateStatusBasedOnStock()` 更新商品狀態

**測試建議:**
1. 測試新增商品時輸入庫存數量
2. 測試編輯商品時修改庫存數量
3. 驗證庫存數量為 0 時商品狀態自動變為缺貨
4. 驗證庫存數量 > 0 時商品狀態自動變為上架中
