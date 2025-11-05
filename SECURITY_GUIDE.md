# 🔐 GCP 金鑰安全配置指南

## ⚠️ 重要安全警告

**絕對不要將 GCP 服務帳號金鑰檔案提交到 Git 或公開的程式碼庫！**

這會導致：
- 🔴 任何人都可以存取您的 GCP 資源
- 🔴 可能造成資料外洩或資源被濫用
- 🔴 產生不必要的 GCP 費用
- 🔴 違反安全最佳實踐

---

## ✅ 已實施的安全措施

### 1. `.gitignore` 已更新
已將以下檔案加入 `.gitignore`：
- `**/luminous-smithy-*.json`
- `**/*-544c644bc77f.json`
- `**/gcp-service-account-key.json`
- `*.json`（除了特定檔案如 `package.json`）

### 2. `application.properties` 已更新
- 支援從環境變數讀取配置
- 金鑰路徑可透過環境變數設定

---

## 🔧 安全的配置方式

### 方法 1: 使用環境變數（推薦 ⭐⭐⭐⭐⭐）

#### 設定環境變數：

```bash
# Linux / macOS
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/luminous-smithy-477304-p9-544c644bc77f.json
export GCP_PROJECT_ID=luminous-smithy-477304-p9
export GCP_BUCKET_NAME=eshopping-images

# Windows
set GOOGLE_APPLICATION_CREDENTIALS=C:\path\to\luminous-smithy-477304-p9-544c644bc77f.json
set GCP_PROJECT_ID=luminous-smithy-477304-p9
set GCP_BUCKET_NAME=eshopping-images
```

#### 修改 `application.properties`：

```properties
# GCP Cloud Storage configuration
gcp.storage.project-id=${GCP_PROJECT_ID:luminous-smithy-477304-p9}
gcp.storage.bucket-name=${GCP_BUCKET_NAME:eshopping-images}
# 留空，使用 GOOGLE_APPLICATION_CREDENTIALS 環境變數
gcp.storage.credentials-path=
```

**優點：**
- ✅ 金鑰檔案完全不會出現在程式碼中
- ✅ 不同環境可以使用不同的金鑰
- ✅ 符合 12-Factor App 原則

---

### 方法 2: 外部配置檔案（適合開發環境）

#### 將金鑰檔案放在專案外部：

```bash
# 例如放在 ~/.gcp/ 目錄
mkdir -p ~/.gcp
cp luminous-smithy-477304-p9-544c644bc77f.json ~/.gcp/

# 修改 application.properties
gcp.storage.credentials-path=file:/home/user/.gcp/luminous-smithy-477304-p9-544c644bc77f.json
```

**優點：**
- ✅ 金鑰檔案不在專案目錄中
- ✅ 不會被意外提交到 Git

---

### 方法 3: 使用 Application Default Credentials (ADC)

如果您的應用程式部署在 GCP 環境中（如 GCE、GKE、Cloud Run），可以使用 ADC：

```properties
# application.properties
gcp.storage.project-id=luminous-smithy-477304-p9
gcp.storage.bucket-name=eshopping-images
# 留空，使用 Application Default Credentials
gcp.storage.credentials-path=
```

**優點：**
- ✅ 完全不需要管理金鑰檔案
- ✅ 安全性最高
- ✅ 適合生產環境

---

## 🚨 如果已經提交到 Git 怎麼辦？

### 立即行動：

1. **撤銷並重新生成 GCP 服務帳號金鑰**
   - 前往 GCP Console → IAM & Admin → Service Accounts
   - 找到 `gcs-uploader@luminous-smithy-477304-p9.iam.gserviceaccount.com`
   - 刪除舊的金鑰
   - 建立新的金鑰

2. **從 Git 歷史中移除金鑰檔案**
   ```bash
   # 從 Git 歷史中移除檔案
   git filter-branch --force --index-filter \
     "git rm --cached --ignore-unmatch src/main/resources/luminous-smithy-477304-p9-544c644bc77f.json" \
     --prune-empty --tag-name-filter cat -- --all
   
   # 強制推送（⚠️ 會重寫歷史，請謹慎操作）
   git push origin --force --all
   ```

3. **更新應用程式使用新的金鑰**

4. **檢查 GitHub 是否有敏感資訊**
   - 檢查所有已推送的 commit
   - 如果發現敏感資訊，考慮使用 GitHub 的 Secret Scanning 功能

---

## 📋 安全檢查清單

### 開發環境：
- [ ] `.gitignore` 已包含金鑰檔案模式
- [ ] 金鑰檔案不在 Git 追蹤中
- [ ] `application.properties` 不包含完整的金鑰路徑或敏感資訊
- [ ] 使用環境變數或外部配置檔案

### 生產環境：
- [ ] 使用環境變數或 Secret Management 服務
- [ ] 金鑰檔案有適當的檔案權限（僅應用程式可讀）
- [ ] 服務帳號使用最小權限原則
- [ ] 定期輪換金鑰

---

## 🔍 驗證配置

### 檢查金鑰檔案是否被 Git 追蹤：

```bash
git ls-files | grep -E "\.json$"
```

如果沒有任何輸出，表示金鑰檔案未被追蹤 ✅

### 檢查 Git 歷史中是否有敏感資訊：

```bash
git log --all --full-history --source -- src/main/resources/*.json
```

---

## 💡 最佳實踐建議

1. **使用環境變數或 Secret Management**
   - 開發環境：環境變數
   - 生產環境：GCP Secret Manager、AWS Secrets Manager 等

2. **最小權限原則**
   - 服務帳號只給予必要的權限
   - 定期審查權限設定

3. **定期輪換金鑰**
   - 每 90 天更換一次服務帳號金鑰
   - 如果懷疑金鑰外洩，立即更換

4. **監控異常活動**
   - 在 GCP Console 中啟用審計日誌
   - 監控服務帳號的使用情況

---

## 📚 參考資源

- [GCP 服務帳號最佳實踐](https://cloud.google.com/iam/docs/best-practices-service-accounts)
- [GitHub Secret Scanning](https://docs.github.com/en/code-security/secret-scanning)
- [OWASP 安全配置指南](https://owasp.org/www-project-secure-coding-practices-quick-reference-guide/)

---

## ⚠️ 重要提醒

**如果您已經將金鑰檔案提交到 GitHub：**

1. 🔴 **立即撤銷並重新生成金鑰**
2. 🔴 **從 Git 歷史中移除敏感資訊**
3. 🔴 **檢查是否有未授權的存取**
4. 🔴 **更新應用程式使用新的金鑰**

安全第一！🚨

