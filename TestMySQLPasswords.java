import java.sql.*;

public class TestMySQLPasswords {
    public static void main(String[] args) {
        String[] passwords = {"", "root", "password", "123456", "1234", "admin"};
        String url = "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String username = "root";
        
        for (String password : passwords) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection(url, username, password);
                System.out.println("Password found: '" + password + "'");
                
                // Test creating musicflow database
                Statement statement = connection.createStatement();
                statement.execute("CREATE DATABASE IF NOT EXISTS musicflow");
                System.out.println("musicflow database created/verified");
                
                connection.close();
                return;
                
            } catch (Exception e) {
                System.out.println("Password '" + password + "' failed: " + e.getMessage());
            }
        }
        
        System.out.println("No valid password found. Please check MySQL root password.");
    }
}