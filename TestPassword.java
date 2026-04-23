import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String password = "Admin@123";
        String hash = "$2a$12$HJGGn88yNAarmw4TynLFUu4NVntTMOtf8mePP0nBQZ7LTgTHeJlba";
        
        boolean matches = encoder.matches(password, hash);
        System.out.println("Password 'Admin@123' matches hash: " + matches);
        
        // Generate a new hash to verify
        String newHash = encoder.encode(password);
        System.out.println("New hash for 'Admin@123': " + newHash);
    }
}
