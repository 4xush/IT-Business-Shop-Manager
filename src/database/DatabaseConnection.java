package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:kingcom.db";

    // Establish database connection
    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        System.out.println("Connected to Database");
        return conn;
    }

    // Create necessary tables
    public static void createTables() {
        String productsTable = "CREATE TABLE IF NOT EXISTS Products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "category TEXT NOT NULL," +
                "price REAL NOT NULL," +
                "stock INTEGER NOT NULL," +
                "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

        String salesTable = "CREATE TABLE IF NOT EXISTS Sales (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_name TEXT NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

        String repairsTable = "CREATE TABLE IF NOT EXISTS Repairs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customerName TEXT NOT NULL, " +
                "phoneNumber TEXT NOT NULL, " +
                "deviceModel TEXT NOT NULL, " +
                "issueDescription TEXT NOT NULL, " +
                "estimatedCost REAL NOT NULL, " +
                "status TEXT DEFAULT 'Pending', " +
                "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

        try (Connection conn = connect();
                PreparedStatement stmt1 = conn.prepareStatement(productsTable);
                PreparedStatement stmt2 = conn.prepareStatement(salesTable);
                PreparedStatement stmt3 = conn.prepareStatement(repairsTable)) {
            stmt1.execute();
            stmt2.execute();
            stmt3.execute();
            System.out.println("Tables Created Successfully");
            System.out.println(
                    "Using database file: " + new java.io.File(URL.replace("jdbc:sqlite:", "")).getAbsolutePath());
        } catch (SQLException e) {
            System.out.println("Error Creating Tables: " + e.getMessage());
        }
    }

    public static void viewTables() {
        String queryProducts = "SELECT * FROM Products";
        String querySales = "SELECT * FROM Sales";
        String queryRepairs = "SELECT * FROM Repairs";

        try (Connection conn = connect();
                PreparedStatement stmtProducts = conn.prepareStatement(queryProducts);
                PreparedStatement stmtSales = conn.prepareStatement(querySales);
                PreparedStatement stmtRepairs = conn.prepareStatement(queryRepairs)) {

            // View Products table
            System.out.println("Products Table:");
            try (var rs = stmtProducts.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("ID: %d, Name: %s, Category: %s, Price: %.2f, Stock: %d, Date: %s%n",
                            rs.getInt("id"), rs.getString("name"), rs.getString("category"),
                            rs.getDouble("price"), rs.getInt("stock"), rs.getString("date_added"));
                }
            }

            // View Sales table
            System.out.println("\nSales Table:");
            try (var rs = stmtSales.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("ID: %d, Product: %s, Quantity: %d, Price: %.2f, Date: %s%n",
                            rs.getInt("id"), rs.getString("product_name"), rs.getInt("quantity"),
                            rs.getDouble("price"), rs.getString("sale_date"));
                }
            }

            // View Repairs table
            System.out.println("\nRepairs Table:");
            try (var rs = stmtRepairs.executeQuery()) {
                while (rs.next()) {
                    System.out.printf(
                            "ID: %d, Customer: %s, Phone: %s, Device: %s, Issue: %s, Cost: %.2f, Status: %s, Date: %s%n",
                            rs.getInt("id"), rs.getString("customerName"), rs.getString("phoneNumber"),
                            rs.getString("deviceModel"), rs.getString("issueDescription"),
                            rs.getDouble("estimatedCost"), rs.getString("status"), rs.getString("createdAt"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error Viewing Tables: " + e.getMessage());
        }
    }

    public static void checkSchema() {
        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("PRAGMA table_info(Repairs);")) {
            System.out.println("Repairs Table Schema:");
            while (rs.next()) {
                System.out.printf("Column: %s, Type: %s, Not Null: %d, Default: %s%n",
                        rs.getString("name"), rs.getString("type"), rs.getInt("notnull"), rs.getString("dflt_value"));
            }
        } catch (SQLException e) {
            System.out.println("Error checking schema: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createTables();
        viewTables();
    }
}
