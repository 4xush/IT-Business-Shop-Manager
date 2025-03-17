package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:kingcom.db"; // SQLite Database
    private static Connection conn = null;
    
    // Establish database connection
    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC"); // Explicitly load driver
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL);
                System.out.println("Connected to Database");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
        }
        return conn;
    }
    // Close database connection
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database Disconnected");
            }
        } catch (SQLException e) {
            System.out.println("Error Closing Database: " + e.getMessage());
        }
    }
    
    // Example: Create Products Table
    public static void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS Products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "category TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "stock INTEGER NOT NULL," +
                "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("Products Table Created");
        } catch (SQLException e) {
            System.out.println("Error Creating Table: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        connect();
        createTables();
        // close();
    }
}
