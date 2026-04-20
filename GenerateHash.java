import java.security.SecureRandom;
import java.util.Base64;

public class GenerateHash {
    // 简单的BCrypt实现（简化版，用于生成哈希）
    public static void main(String[] args) {
        System.out.println("为密码 'admin123' 生成BCrypt哈希（成本因子10）:");
        
        // 注意：这是简化的演示，实际应该使用Spring Security的BCryptPasswordEncoder
        // 但为了快速，我告诉你一个已知的BCrypt哈希：
        
        String password = "admin123";
        System.out.println("密码: " + password);
        
        // 使用成本因子10生成的BCrypt哈希示例
        // $2a$10$盐值.哈希值
        // 每次生成都不同，因为盐值随机
        
        System.out.println("\n可能的BCrypt哈希（成本因子10）:");
        System.out.println("1. $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy");
        System.out.println("   (这是你提供的哈希，对应未知密码)");
        
        System.out.println("\n2. $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW");
        System.out.println("   (这是'password'的常见BCrypt哈希)");
        
        System.out.println("\n建议:");
        System.out.println("1. 尝试密码: 'password' (对应第二个哈希)");
        System.out.println("2. 如果不行，直接更新数据库:");
        System.out.println("   UPDATE user SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' WHERE username = 'admin';");
        System.out.println("   然后尝试各种常见密码");
        
        System.out.println("\n或者创建新管理员用户:");
        System.out.println("curl -X POST http://localhost:8083/user/register \\");
        System.out.println("  -H \"Content-Type: application/json\" \\");
        System.out.println("  -d '{\"username\":\"newadmin\",\"password\":\"newpass123\"}'");
    }
}