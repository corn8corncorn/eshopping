## Spring Security 配置修復說明

### 問題描述
```
嚴重: Exception starting filter [springSecurityFilterChain]
org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'springSecurityFilterChain' available
```

### 根本原因
1. `SecurityConfig` 沒有被正確載入到 root application context
2. `web.xml` 中的配置路徑過於寬泛
3. Filter 順序可能不正確

### 修復內容

#### 1. 更新 `AppConfig.java`
- 使用 `@Import` 明確導入 `SecurityConfig` 和 `HibernateConfig`
- 確保配置類在 root context 中正確載入

```java
@Configuration
@ComponentScan(basePackages = "com.example.demo")
@EnableTransactionManagement
@Import({SecurityConfig.class, HibernateConfig.class})
public class AppConfig {
}
```

#### 2. 更新 `web.xml`
- 明確指定 `AppConfig` 作為配置類（而不是包路徑）
- 調整 filter 順序：Spring Security filter 放在第一位
- 添加 dispatcher 配置確保正確攔截

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>com.example.demo.config.AppConfig</param-value>
</context-param>
```

#### 3. Filter 配置順序
- Spring Security Filter（第一）
- Character Encoding Filter（第二）

### 驗證步驟
1. 重新編譯專案：`mvn clean compile`
2. 打包 WAR 檔案：`mvn clean package`
3. 重新部署到 Tomcat
4. 檢查啟動日誌，確認不再出現 `NoSuchBeanDefinitionException`

### 注意事項
- `SecurityConfig` 必須在 root application context 中，不能在 DispatcherServlet 的 child context
- 確保 `CustomUserDetailsService` 有 `@Service` 註解並被正確掃描
- Filter 順序很重要，Spring Security filter 應該在其他 filter 之前

### 如果問題仍然存在
1. 檢查 Tomcat 日誌中的完整錯誤訊息
2. 確認 Spring Security 相關的 jar 包都在 `WEB-INF/lib` 中
3. 驗證 `CustomUserDetailsService` 是否能被正確注入到 `SecurityConfig`
4. 檢查是否有循環依賴問題

