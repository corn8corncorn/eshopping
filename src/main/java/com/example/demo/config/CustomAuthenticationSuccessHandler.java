package com.example.demo.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 自定義登入成功處理器
 * 根據用戶角色導向不同頁面：
 * - ADMIN → 後台管理頁面 (/admin/dashboard)
 * - USER → 購物網站首頁 (/home)
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        logger.info("用戶登入成功 - username: {}, authorities: {}", username, authorities);
        
        // 詳細記錄所有權限
        for (GrantedAuthority authority : authorities) {
            logger.info("權限檢查 - authority: {}, equals ROLE_ADMIN: {}", 
                       authority.getAuthority(), 
                       authority.getAuthority().equals("ROLE_ADMIN"));
        }
        
        // 檢查用戶角色（多種方式檢查以確保正確識別）
        boolean isAdmin = authorities.stream()
            .anyMatch(a -> {
                String auth = a.getAuthority();
                boolean matches = auth.equals("ROLE_ADMIN");
                logger.debug("檢查權限 - authority: {}, matches: {}", auth, matches);
                return matches;
            });
        
        String targetUrl;
        if (isAdmin) {
            // 管理員導向後台管理頁面
            targetUrl = "/admin/dashboard";
            logger.info("✓ 管理員登入，導向後台管理頁面 - username: {}, targetUrl: {}", username, targetUrl);
        } else {
            // 一般用戶導向購物網站首頁
            targetUrl = "/home";
            logger.info("✓ 一般用戶登入，導向購物網站首頁 - username: {}, targetUrl: {}", username, targetUrl);
        }
        
        // 重定向到目標頁面（確保包含 context path）
        String redirectUrl = request.getContextPath() + targetUrl;
        logger.info("重定向 URL: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}

