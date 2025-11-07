package com.example.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt 密碼生成工具
 * 用於生成 BCrypt 加密後的密碼，供手動在資料庫中創建管理員帳號使用
 * 
 * 使用方法：
 * 1. 運行此類的 main 方法
 * 2. 輸入您想要的密碼
 * 3. 複製輸出的 BCrypt 值
 * 4. 在 SQL INSERT 語句中使用該值
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 修改這裡的密碼為您想要的密碼
        String[] passwords = {
            "admin123",      // 預設管理員密碼
            "password",      // 範例密碼
            "your_password"  // 您的自訂密碼
        };
        
        System.out.println("============================================");
        System.out.println("BCrypt 密碼生成工具");
        System.out.println("============================================");
        System.out.println();
        
        for (String password : passwords) {
            String encodedPassword = encoder.encode(password);
            System.out.println("原始密碼: " + password);
            System.out.println("BCrypt 值: " + encodedPassword);
            System.out.println();
        }
        
        System.out.println("============================================");
        System.out.println("使用說明：");
        System.out.println("1. 複製上面的 BCrypt 值");
        System.out.println("2. 在 SQL INSERT 語句中使用該值");
        System.out.println("3. 範例 SQL：");
        System.out.println("   INSERT INTO users (username, email, password, role, is_enabled, created_at, updated_at)");
        System.out.println("   VALUES ('admin', 'admin@example.com', 'BCrypt值', 'ADMIN', true, NOW(), NOW());");
        System.out.println("============================================");
    }
    
    /**
     * 生成單個密碼的 BCrypt 值
     * @param password 原始密碼
     * @return BCrypt 加密後的密碼
     */
    public static String generateBCrypt(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}

