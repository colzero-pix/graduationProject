import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String storedHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
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
        
        System.out.println("测试密码匹配:");
        for (String password : testPasswords) {
            boolean matches = encoder.matches(password, storedHash);
            System.out.println(password + " : " + (matches ? "✓ 匹配" : "✗ 不匹配"));
        }
    }
}