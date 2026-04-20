import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UpdateAdminPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPassword = "123456";
        
        // 生成123456的BCrypt哈希
        String hashedPassword = encoder.encode(newPassword);
        
        System.out.println("新密码: " + newPassword);
        System.out.println("BCrypt哈希: " + hashedPassword);
        System.out.println("\nSQL更新语句:");
        System.out.println("UPDATE user SET password = '" + hashedPassword + "' WHERE username = 'admin';");
        
        // 验证生成的哈希
        boolean matches = encoder.matches(newPassword, hashedPassword);
        System.out.println("\n验证: 密码匹配 = " + matches);
    }
}