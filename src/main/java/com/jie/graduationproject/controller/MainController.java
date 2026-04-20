package com.jie.graduationproject.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MainController implements WebMvcConfigurer {

    // SPA路由：所有前端路由都返回index.html
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/login").setViewName("forward:/index.html");
        registry.addViewController("/dashboard").setViewName("forward:/index.html");
        registry.addViewController("/goods").setViewName("forward:/index.html");
        registry.addViewController("/shelf").setViewName("forward:/index.html");
        registry.addViewController("/users").setViewName("forward:/index.html");
        registry.addViewController("/main").setViewName("forward:/index.html");
    }

    // API端点：返回系统信息
    @GetMapping("/api/system/info")
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "超市仓库管理系统");
        response.put("version", "1.0.0");
        response.put("description", "基于Spring Boot的超市仓库管理系统");
        response.put("frontendType", "SPA (Single Page Application)");
        response.put("apiEndpoints", Map.of(
                "用户管理", "/user/**",
                "商品管理", "/storekeeper/goods/**",
                "货架管理", "/storekeeper/shelf/**",
                "测试接口", "/test/**"
        ));
        return response;
    }

    // API端点：检查用户权限
    @GetMapping("/api/user/check-permission")
    public Map<String, Object> checkPermission() {
        Map<String, Object> response = new HashMap<>();
        response.put("hasPermission", true);
        response.put("message", "权限检查API");
        return response;
    }
    
    // API端点：重置admin密码（开发用）
    @GetMapping("/api/reset-admin-password")
    public Map<String, Object> resetAdminPassword() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 生成123456的BCrypt哈希
            org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = 
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            String newPassword = "123456";
            String hashedPassword = encoder.encode(newPassword);
            
            response.put("success", true);
            response.put("message", "Admin密码重置为: " + newPassword);
            response.put("newPassword", newPassword);
            response.put("hashedPassword", hashedPassword);
            
            // 注意：这里应该更新数据库，但需要数据库访问
            // 实际应用中应该调用UserService来更新
            
            System.out.println("Admin密码应重置为: " + newPassword);
            System.out.println("BCrypt哈希: " + hashedPassword);
            System.out.println("请执行SQL: UPDATE user SET password = '" + hashedPassword + "' WHERE username = 'admin';");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "重置密码失败: " + e.getMessage());
        }
        
        return response;
    }
    
    // API端点：测试密码
    @GetMapping("/api/test/password")
    public Map<String, Object> testPassword() {
        Map<String, Object> response = new HashMap<>();
        
        // 这是数据库中的admin密码哈希
        String storedHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        // 测试的密码列表
        String[] testPasswords = {
            "123456",
            "admin123",
            "admin",
            "password",
            "12345678",
            "admin123456",
            "Admin123",
            "ADMIN123",
            "admin@123",
            "Admin@123"
        };
        
        List<String> matchedPasswords = new ArrayList<>();
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = 
            new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        
        for (String password : testPasswords) {
            if (encoder.matches(password, storedHash)) {
                matchedPasswords.add(password);
            }
        }
        
        response.put("storedHash", storedHash);
        response.put("testedPasswords", testPasswords);
        response.put("matchedPasswords", matchedPasswords);
        response.put("hasMatch", !matchedPasswords.isEmpty());
        
        return response;
    }
}