## Git Commit Message

### Type: fix
### Scope: test-files
### Subject: 修正測試檔案中的編譯錯誤

### Body:
修正 CartModelTest、OrderDAOTest、OrderModelTest、OrderServiceTest 中的編譯錯誤，確保測試檔案符合實際模型類的 API 設計。

**主要變更:**

1. **CartModelTest.java**
   - 移除 `CartItem.updateUnitPrice()` 方法調用
     - 原因：`CartItem` 的單價是從 `Product` 動態獲取的，不需要手動更新
     - 調整：測試商品價格變更後，驗證 `CartItem` 會自動從 `Product` 獲取新價格
   - 移除 `CartItem.syncProductInfo()` 方法調用
     - 原因：`CartItem` 沒有此方法，它會自動從 `Product` 獲取資訊
     - 調整：改為測試動態獲取商品資訊的功能
   - 移除 `Cart.calculateTotalAmount()` 方法調用
     - 原因：`Cart.getTotalAmount()` 是動態計算的，不需要手動調用
     - 調整：移除不必要的調用，直接驗證 `getTotalAmount()` 的結果

2. **OrderDAOTest.java**
   - 修正 `Order` 構造函數調用
     - 舊：`new Order(customer, "收件人", "台北市信義區", Order.PaymentMethod.CREDIT_CARD)`
     - 新：`new Order(customer, Order.PaymentMethod.CREDIT_CARD)`
   - 添加 `OrderAddress` 的建立和設置
     - 使用 `OrderAddress` 構造函數建立訂單地址
     - 通過 `order.setOrderAddress()` 設置到訂單中
   - 添加 `OrderAddress` 的 import 語句

3. **OrderModelTest.java**
   - 修正 `Order` 構造函數調用：改為 2 參數構造函數
   - 添加 `OrderAddress` 的建立和設置
   - 添加 `OrderAddress` 的 import 語句

4. **OrderServiceTest.java**
   - 修正所有 `Order` 構造函數調用：將 4 參數改為 2 參數
   - 在所有建立訂單的地方添加 `OrderAddress` 的建立和設置
   - 添加 `OrderAddress` 的 import 語句

**問題修復:**

- **問題**: 測試檔案無法編譯，出現多個編譯錯誤
  - **原因**: 
    1. 測試檔案使用了不存在的 `CartItem` 方法（`updateUnitPrice()`, `syncProductInfo()`）
    2. 測試檔案使用了不存在的 `Cart` 方法（`calculateTotalAmount()`）
    3. 測試檔案使用了錯誤的 `Order` 構造函數（4 參數而非 2 參數）
  - **解決**: 
    1. 移除不存在的方法調用，改為測試實際的動態獲取功能
    2. 修正 `Order` 構造函數調用，並正確設置 `OrderAddress`
    3. 確保所有測試符合實際模型類的 API 設計

**技術細節:**

- `CartItem` 的 `getUnitPrice()` 和 `getProductName()` 是動態方法，從關聯的 `Product` 獲取資訊
- `Cart.getTotalAmount()` 是動態計算方法，遍歷所有 `CartItem` 並累加小計
- `Order` 構造函數只需要 `Customer` 和 `PaymentMethod` 兩個參數
- `OrderAddress` 是獨立的實體類，需要單獨建立並設置到 `Order` 中

**驗證:**

- 所有測試檔案已成功編譯，無語法錯誤
- 使用 `mvn test-compile` 驗證編譯成功
