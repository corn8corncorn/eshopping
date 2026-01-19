# 為什麼 Order.status 使用 OrderStatus 枚舉（Enum）？

## 📋 問題：為什麼不用 boolean、int 或 String？

### ❌ 方案一：使用 boolean
```java
private boolean isPending;  // 待處理
private boolean isShipped;  // 已出貨
private boolean isDelivered; // 已送達
```

**缺點：**
- 多個狀態需要多個 boolean 變數，容易產生矛盾（例如同時為 true）
- 無法表達所有狀態組合
- 程式碼複雜，難以維護

### ❌ 方案二：使用 int
```java
private int status;  // 0=待處理, 1=已確認, 2=處理中, 3=已出貨...
```

**缺點：**
- **魔法數字（Magic Numbers）**：程式碼中出現 `if (status == 3)` 難以理解
- **容易出錯**：可能傳入無效數字（如 999）
- **沒有編譯時檢查**：編譯器無法發現錯誤
- **可讀性差**：需要查文件才知道 3 代表什麼

### ❌ 方案三：使用 String
```java
private String status;  // "PENDING", "SHIPPED", "DELIVERED"
```

**缺點：**
- **拼字錯誤風險**：`"PENDIN"` vs `"PENDING"`（少一個 G）
- **大小寫不一致**：`"pending"` vs `"PENDING"` vs `"Pending"`
- **沒有編譯時檢查**：錯誤的狀態字串要到執行時才發現
- **效能較差**：字串比較比枚舉慢
- **無法使用 switch-case**（Java 7 以前）

## ✅ 方案四：使用 Enum（最佳實踐）

```java
@Enumerated(EnumType.STRING)
@Column(name = "status", nullable = false)
private OrderStatus status = OrderStatus.PENDING;

public enum OrderStatus {
    PENDING("待處理"),
    CONFIRMED("已確認"),
    PROCESSING("處理中"),
    SHIPPED("已出貨"),
    DELIVERED("已送達"),
    CANCELLED("已取消"),
    RETURNED("已退貨");
    
    private final String description;
    
    OrderStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
```

## 🎯 使用 Enum 的優點

### 1. **型別安全（Type Safety）**
```java
// ✅ 正確：編譯器會檢查
order.setStatus(OrderStatus.PENDING);

// ❌ 錯誤：編譯時就會發現錯誤
order.setStatus("PENDIN");  // 編譯錯誤！
order.setStatus(999);       // 編譯錯誤！
```

### 2. **可讀性高**
```java
// ✅ 清楚明瞭
if (order.getStatus() == OrderStatus.PENDING) {
    // 處理待處理訂單
}

// ❌ 難以理解
if (order.getStatus() == 0) {
    // 0 是什麼意思？
}
```

### 3. **IDE 自動完成**
- 輸入 `OrderStatus.` 時，IDE 會自動列出所有可用狀態
- 減少拼字錯誤

### 4. **可以使用 switch-case**
```java
switch (order.getStatus()) {
    case PENDING:
        // 處理待處理訂單
        break;
    case SHIPPED:
        // 處理已出貨訂單
        break;
    default:
        // 其他狀態
}
```

### 5. **可以附加額外資訊**
```java
// 每個狀態可以有自己的描述
OrderStatus.PENDING.getDescription();  // 返回 "待處理"
OrderStatus.SHIPPED.getDescription();  // 返回 "已出貨"
```

### 6. **資料庫儲存方式**
```java
@Enumerated(EnumType.STRING)  // 儲存為 "PENDING", "SHIPPED" 等字串
// 或
@Enumerated(EnumType.ORDINAL)  // 儲存為 0, 1, 2 等數字（不建議）
```

**建議使用 `EnumType.STRING`**：
- 資料庫中直接看到狀態名稱，易於除錯
- 即使新增狀態，也不會影響現有資料

### 7. **可以實作狀態轉換邏輯**
```java
public enum OrderStatus {
    PENDING("待處理"),
    SHIPPED("已出貨");
    
    public boolean canTransitionTo(OrderStatus newStatus) {
        // 定義狀態轉換規則
        if (this == PENDING && newStatus == SHIPPED) {
            return true;
        }
        return false;
    }
}
```

## 📊 比較表

| 特性 | boolean | int | String | **Enum** |
|------|---------|-----|--------|----------|
| 型別安全 | ❌ | ❌ | ❌ | ✅ |
| 可讀性 | ❌ | ❌ | ⚠️ | ✅ |
| IDE 支援 | ⚠️ | ❌ | ⚠️ | ✅ |
| 編譯時檢查 | ❌ | ❌ | ❌ | ✅ |
| 效能 | ✅ | ✅ | ❌ | ✅ |
| 維護性 | ❌ | ❌ | ❌ | ✅ |
| 可擴展性 | ❌ | ⚠️ | ⚠️ | ✅ |

## 🎓 面試回答範例

**面試官：為什麼 Order 的 status 使用 enum 而不是 String 或 int？**

**回答：**

「我選擇使用 enum 主要有以下幾個原因：

1. **型別安全**：使用 enum 可以在編譯時就發現錯誤，避免傳入無效的狀態值。如果使用 String，可能會出現拼字錯誤（如 "PENDIN" 而不是 "PENDING"），這些錯誤要到執行時才會發現。

2. **可讀性**：`OrderStatus.PENDING` 比字串 `"PENDING"` 或數字 `0` 更清楚，程式碼更容易理解和維護。

3. **IDE 支援**：使用 enum 時，IDE 可以提供自動完成功能，減少輸入錯誤。

4. **擴展性**：enum 可以附加額外資訊，例如我在 OrderStatus 中加入了 `description` 欄位，可以同時儲存中文描述，方便前端顯示。

5. **資料庫儲存**：使用 `@Enumerated(EnumType.STRING)` 將 enum 儲存為字串，資料庫中可以直接看到狀態名稱，便於除錯和查詢。

這是業界公認的最佳實踐，也是 Java 官方推薦的做法。」

## 📝 實際應用範例

在 eShopping 專案中，除了 OrderStatus，還有：

- **PaymentMethod**（付款方式）：CREDIT_CARD, BANK_TRANSFER, CASH_ON_DELIVERY
- **PaymentStatus**（付款狀態）：PENDING, PAID, FAILED, REFUNDED
- **ProductStatus**（商品狀態）：ACTIVE, INACTIVE, OUT_OF_STOCK

這些都使用 enum，確保型別安全和程式碼品質。


