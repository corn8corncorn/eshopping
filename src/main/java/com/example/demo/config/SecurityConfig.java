package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.config.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home", "/register", "/login", "/forgot-password", "/reset-password", "/shop/**", "/shop/search**", "/css/**", "/js/**", "/images/**", "/resources/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/users/**", "/customers").hasRole("ADMIN")
                // 用戶訂單相關路由（必須在管理員路由之前，因為更具體）
                .antMatchers("/orders/my", "/orders/my/**").hasRole("USER")
                .antMatchers("/orders/checkout", "/orders/create", "/orders/confirmation/**").hasRole("USER")
                .antMatchers("/orders/*/cancel").hasRole("USER")
                // 管理員訂單相關路由
                .antMatchers("/orders", "/orders/*/update-status", "/orders/*/update-payment-status").hasRole("ADMIN")
                .antMatchers("/orders/{id}").hasAnyRole("ADMIN", "USER") // 管理員和用戶都可以查看訂單詳情
                .antMatchers("/products/add", "/products/edit/**", "/products/delete/**", "/products/update/**", "/products/save").hasRole("ADMIN")
                .antMatchers("/products", "/products/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/cart/**", "/customers/edit", "/customers/profile", "/account/**").hasRole("USER")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            .and()
                .csrf().disable(); // 暫時關閉CSRF以便測試
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (userDetailsService != null) {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}