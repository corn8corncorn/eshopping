package com.example.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 根應用程式配置類
 * 包含所有需要在 root application context 中載入的配置
 */
@Configuration
@ComponentScan(basePackages = "com.example.demo")
@EnableTransactionManagement
@Import({SecurityConfig.class, HibernateConfig.class})
public class AppConfig {
}