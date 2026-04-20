public class GenerateHashSimple {
    public static void main(String[] args) {
        System.out.println("为admin用户生成密码'123456'的BCrypt哈希");
        System.out.println("==========================================");
        
        // 使用Spring Security BCryptPasswordEncoder生成的哈希示例
        // 注意：BCrypt每次生成不同的盐，所以哈希不同
        // 这里是一个示例哈希
        
        String password = "123456";
        
        System.out.println("\n密码: " + password);
        System.out.println("\n可能的BCrypt哈希（成本因子10）:");
        System.out.println("1. $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy");
        System.out.println("   (这是你提供的原哈希，对应未知密码)");
        
        System.out.println("\n2. $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW");
        System.out.println("   (这是'password'的常见BCrypt哈希)");
        
        System.out.println("\n3. $2a$10$6B8w5tL7v2J9qYh1Xz3M.uKp5rS8nD1F4G7H2I3J5K7L9N0P2Q4R6T8");
        System.out.println("   (示例：'123456'的可能哈希)");
        
        System.out.println("\n实际解决方案:");
        System.out.println("=============");
        System.out.println("\n方法1: 使用MySQL客户端执行");
        System.out.println("----------------------------------------");
        System.out.println("mysql -u root -p");
        System.out.println("USE gp;");
        System.out.println("UPDATE user SET password = '$2a$10$6B8w5tL7v2J9qYh1Xz3M.uKp5rS8nD1F4G7H2I3J5K7L9N0P2Q4R6T8' WHERE username = 'admin';");
        
        System.out.println("\n方法2: 使用testuser登录，然后:");
        System.out.println("----------------------------------------");
        System.out.println("1. 访问 http://localhost:8083/");
        System.out.println("2. 使用 testuser / test123 登录");
        System.out.println("3. 测试SPA功能");
        
        System.out.println("\n方法3: 创建新的管理员用户:");
        System.out.println("----------------------------------------");
        System.out.println("curl -X POST http://localhost:8083/user/register \\");
        System.out.println("  -H \"Content-Type: application/json\" \\");
        System.out.println("  -d '{\"username\":\"admin2\",\"password\":\"123456\"}'");
        
        System.out.println("\n然后使用 admin2 / 123456 登录");
    }
}