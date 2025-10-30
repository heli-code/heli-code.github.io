import java.sql.*;

public class TestMySQLFinal {
    public static void main(String[] args) {
        String systemUrl = "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String musicflowUrl = "jdbc:mysql://localhost:3306/musicflow?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "31415926535abc@";
        
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // First connect to system database
            Connection systemConnection = DriverManager.getConnection(systemUrl, username, password);
            System.out.println("MySQL system database connection successful!");
            
            // Check if musicflow database exists
            Statement statement = systemConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE 'musicflow'");
            
            if (resultSet.next()) {
                System.out.println("musicflow database exists");
            } else {
                System.out.println("musicflow database does not exist, creating...");
                statement.execute("CREATE DATABASE musicflow");
                System.out.println("musicflow database created successfully");
            }
            
            systemConnection.close();
            
            // Now connect to musicflow database
            Connection musicflowConnection = DriverManager.getConnection(musicflowUrl, username, password);
            System.out.println("musicflow database connection successful!");
            
            // Check tables
            Statement musicflowStatement = musicflowConnection.createStatement();
            resultSet = musicflowStatement.executeQuery("SHOW TABLES");
            System.out.println("Database table query successful");
            
            musicflowConnection.close();
            System.out.println("All database connections closed");
            
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
}