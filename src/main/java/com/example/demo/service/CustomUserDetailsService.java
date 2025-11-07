package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 以使用者名稱進行查詢（與目前系統的登入邏輯一致）
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 記錄用戶角色資訊（用於調試）
        String roleName = user.getRole() != null ? user.getRole().name() : "UNKNOWN";
        String authority = "ROLE_" + roleName;
        System.out.println("=== 用戶角色資訊 ===");
        System.out.println("Username: " + username);
        System.out.println("Role enum: " + user.getRole());
        System.out.println("Role name: " + roleName);
        System.out.println("Authority: " + authority);
        System.out.println("==================");

        // 使用 Spring Security 內建的 User.builder() 來建立 UserDetails
        // enabled 狀態由 is_enabled 欄位控制
        // accountExpired, accountLocked, credentialsExpired 都設為 false (不過期/不鎖定)
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authority)
                .accountExpired(false)      // 帳號未過期
                .accountLocked(false)        // 帳號未鎖定
                .credentialsExpired(false)   // 憑證未過期
                .disabled(!Boolean.TRUE.equals(user.getEnabled()))  // 是否停用
                .build();
    }
}
