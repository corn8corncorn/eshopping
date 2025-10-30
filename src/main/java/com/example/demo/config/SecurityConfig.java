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

@Configuration
@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/home", "/register", "/login", "/forgot-password", "/reset-password", "/shop/**", "/css/**", "/js/**", "/images/**", "/resources/**").permitAll()
                .antMatchers("/users/**", "/customers").hasRole("ADMIN")
                .antMatchers("/orders", "/orders/*/update-status", "/orders/*/update-payment-status").hasRole("ADMIN")
                .antMatchers("/products/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/cart/**", "/orders/**", "/customers/edit", "/customers/profile", "/account/**").hasRole("USER")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
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