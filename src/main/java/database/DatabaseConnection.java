package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:shopsync.db";
    private static Connection conn; // Store connection for reuse

    // Establish database connection
    public static Connection connect() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connected to Database");
        }
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

        String saleGroupsTable = "CREATE TABLE IF NOT EXISTS SaleGroups (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "saleTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "status TEXT DEFAULT 'Pending');";

        String salesTable = "CREATE TABLE IF NOT EXISTS Sales (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "saleGroupId INTEGER NOT NULL," +
                "productId INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "totalAmount REAL NOT NULL," +
                "saleTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "status TEXT DEFAULT 'Pending'," +
                "FOREIGN KEY (saleGroupId) REFERENCES SaleGroups(id)," +
                "FOREIGN KEY (productId) REFERENCES Products(id));";

        String repairsTable = "CREATE TABLE IF NOT EXISTS Repairs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customerName TEXT NOT NULL, " +
                "phoneNumber TEXT NOT NULL, " +
                "deviceModel TEXT NOT NULL, " +
                "issueDescription TEXT NOT NULL, " +
                "estimatedCost REAL NOT NULL, " +
                "status TEXT DEFAULT 'Pending', " +
                "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        String rechargeTable = "CREATE TABLE IF NOT EXISTS Recharges (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customerMobile TEXT NOT NULL, " +
                "operator TEXT NOT NULL, " +
                "amount REAL NOT NULL, " +
                "status TEXT DEFAULT 'Pending', " +
                "requestTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

        try (Connection conn = connect();
                PreparedStatement stmt1 = conn.prepareStatement(productsTable);
                PreparedStatement stmt2 = conn.prepareStatement(saleGroupsTable);
                PreparedStatement stmt3 = conn.prepareStatement(salesTable);
                PreparedStatement stmt4 = conn.prepareStatement(repairsTable);
                PreparedStatement stmt5 = conn.prepareStatement(rechargeTable)) {
            stmt1.execute();
            stmt2.execute();
            stmt3.execute();
            stmt4.execute();
            stmt5.execute();
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

    // Export the database to a user-specified file
    public static void exportDatabase(File destination) throws Exception {
        File source = new File("shopsync.db"); // Current DB file
        if (!source.exists()) {
            throw new Exception("Database file 'shopsync.db' not found!");
        }
        Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    // Load a database from a user-specified file
    public static void loadDatabase(File source) throws Exception {
        File destination = new File("shopsync.db"); // Current DB file
        if (!source.exists()) {
            throw new Exception("Source database file not found!");
        }
        // Close any existing connection before overwriting
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        // Reconnect to the new database
        connect();
    }

    public static void main(String[] args) {
        createTables();
        viewTables();
    }
}