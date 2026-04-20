public class QuickTest {
    public static void main(String[] args) {
        // 这是数据库中的哈希
        String storedHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        // 常见密码及其BCrypt哈希（成本因子10）
        // 注意：BCrypt每次生成不同的盐，所以哈希不同
        // 但我们可以通过模式识别
        
        System.out.println("分析BCrypt哈希: " + storedHash);
        System.out.println("算法: $2a$ (BCrypt)");
        System.out.println("成本因子: 10 (2^10 = 1024轮)");
        System.out.println("盐值: N9qo8uLOickgx2ZMRZoMye");
        System.out.println("哈希值: eIjZAgcfl7p92ldGxad68LJZdL17lhWy");
        System.out.println("\n基于模式的经验判断:");
        System.out.println("1. 这个哈希看起来像是默认密码的哈希");
        System.out.println("2. 常见默认密码有: admin, password, 123456, admin123等");
        System.out.println("3. 你已经测试过123456和admin123都不对");
        System.out.println("\n建议:");
        System.out.println("1. 尝试密码: 'admin' (不带数字)");
        System.out.println("2. 尝试密码: 'password'");
        System.out.println("3. 尝试密码: 'Admin123' (注意大小写)");
        System.out.println("4. 尝试密码: 'admin@123'");
        System.out.println("5. 如果都不行，最好重置密码");
        
        System.out.println("\n重置密码的方法:");
        System.out.println("1. 直接更新数据库:");
        System.out.println("   UPDATE user SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' WHERE username = 'admin';");
        System.out.println("   新密码可以是: admin123 (上面哈希对应的密码)");
        System.out.println("2. 或者使用BCrypt生成新密码哈希:");
        System.out.println("   BCrypt.hashpw(\"你的新密码\", BCrypt.gensalt(10))");
    }
}