import java.sql.*;

public class TestMySQLConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/musicflow?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "123456";
        
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish connection
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("MySQL database connection successful!");
            
            // Check database
            Statement statement = connection.createStatement();
            
            // Check if musicflow database exists
            ResultSet resultSet = statement.executeQuery("SHOW DATABASES LIKE 'musicflow'");
            if (resultSet.next()) {
                System.out.println("musicflow database exists");
            } else {
                System.out.println("musicflow database does not exist, creating...");
                statement.execute("CREATE DATABASE musicflow");
                System.out.println("musicflow database created successfully");
            }
            
            // Switch to musicflow database
            statement.execute("USE musicflow");
            System.out.println("Switched to musicflow database");
            
            // Check tables
            resultSet = statement.executeQuery("SHOW TABLES");
            System.out.println("Database table query successful");
            
            // Close connection
            connection.close();
            System.out.println("Database connection closed");
            
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}