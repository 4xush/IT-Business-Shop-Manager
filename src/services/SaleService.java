package services;

import database.DatabaseConnection;
import models.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleService {

    public static int createSaleGroup() {
        String sql = "INSERT INTO SaleGroups (saleTime, status) VALUES (?, 'Pending')";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the new saleGroupId
            }
            return -1;
        } catch (SQLException e) {
            System.out.println("Error creating sale group: " + e.getMessage());
            return -1;
        }
    }

    public static boolean addSaleItem(int saleGroupId, int productId, int quantity) {
        String stockCheckSQL = "SELECT stock, price FROM Products WHERE id = ?";
        String insertSaleSQL = "INSERT INTO Sales (saleGroupId, productId, quantity, totalAmount, saleTime, status) VALUES (?, ?, ?, ?, ?, 'Pending')";
        String updateStockSQL = "UPDATE Products SET stock = stock - ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stockCheckStmt = conn.prepareStatement(stockCheckSQL);
             PreparedStatement insertSaleStmt = conn.prepareStatement(insertSaleSQL);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL)) {

            stockCheckStmt.setInt(1, productId);
            ResultSet rs = stockCheckStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Product not found!");
                return false;
            }

            int availableStock = rs.getInt("stock");
            double price = rs.getDouble("price");

            if (availableStock < quantity) {
                System.out.println("Error: Not enough stock available!");
                return false;
            }

            double totalAmount = quantity * price;
            Timestamp saleTime = new Timestamp(System.currentTimeMillis());

            insertSaleStmt.setInt(1, saleGroupId);
            insertSaleStmt.setInt(2, productId);
            insertSaleStmt.setInt(3, quantity);
            insertSaleStmt.setDouble(4, totalAmount);
            insertSaleStmt.setTimestamp(5, saleTime);
            insertSaleStmt.executeUpdate();

            updateStockStmt.setInt(1, quantity);
            updateStockStmt.setInt(2, productId);
            updateStockStmt.executeUpdate();

            System.out.println("Sale item added successfully!");
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding sale item: " + e.getMessage());
            return false;
        }
    }

    public static List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT s.id, s.saleGroupId, s.productId, p.name AS productName, s.quantity, s.totalAmount, s.saleTime, s.status " +
                     "FROM Sales s " +
                     "JOIN Products p ON s.productId = p.id " +
                     "WHERE s.saleGroupId IS NOT NULL";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sales.add(new Sale(
                        rs.getInt("id"),
                        rs.getInt("saleGroupId"),
                        rs.getInt("productId"),
                        rs.getString("productName"), // Fetch product name
                        rs.getInt("quantity"),
                        rs.getDouble("totalAmount"),
                        rs.getTimestamp("saleTime"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching sales: " + e.getMessage());
        }
        return sales;
    }

    public static boolean updateSaleItem(int saleId, int newQuantity) {
        String getSaleSQL = "SELECT saleGroupId, productId, quantity, status FROM Sales WHERE id = ?";
        String updateSaleSQL = "UPDATE Sales SET quantity = ?, totalAmount = ? WHERE id = ?";
        String updateStockSQL = "UPDATE Products SET stock = stock + ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement getSaleStmt = conn.prepareStatement(getSaleSQL);
             PreparedStatement updateSaleStmt = conn.prepareStatement(updateSaleSQL);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL)) {

            getSaleStmt.setInt(1, saleId);
            ResultSet rs = getSaleStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Sale item not found!");
                return false;
            }

            if ("Confirmed".equals(rs.getString("status"))) {
                System.out.println("Error: Cannot update a confirmed sale!");
                return false;
            }

            int productId = rs.getInt("productId");
            int oldQuantity = rs.getInt("quantity");
            int quantityDifference = oldQuantity - newQuantity;

            updateStockStmt.setInt(1, quantityDifference);
            updateStockStmt.setInt(2, productId);
            updateStockStmt.executeUpdate();

            double newTotalAmount = newQuantity * getProductPrice(productId);
            updateSaleStmt.setInt(1, newQuantity);
            updateSaleStmt.setDouble(2, newTotalAmount);
            updateSaleStmt.setInt(3, saleId);
            updateSaleStmt.executeUpdate();

            System.out.println("Sale item updated successfully!");
            return true;

        } catch (SQLException e) {
            System.out.println("Error updating sale item: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteSaleItem(int saleId) {
        String getSaleSQL = "SELECT saleGroupId, productId, quantity, status FROM Sales WHERE id = ?";
        String deleteSaleSQL = "DELETE FROM Sales WHERE id = ?";
        String updateStockSQL = "UPDATE Products SET stock = stock + ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement getSaleStmt = conn.prepareStatement(getSaleSQL);
             PreparedStatement deleteSaleStmt = conn.prepareStatement(deleteSaleSQL);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL)) {

            getSaleStmt.setInt(1, saleId);
            ResultSet rs = getSaleStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Sale item not found!");
                return false;
            }

            if ("Confirmed".equals(rs.getString("status"))) {
                System.out.println("Error: Cannot delete a confirmed sale item!");
                return false;
            }

            int productId = rs.getInt("productId");
            int quantity = rs.getInt("quantity");

            deleteSaleStmt.setInt(1, saleId);
            deleteSaleStmt.executeUpdate();

            updateStockStmt.setInt(1, quantity);
            updateStockStmt.setInt(2, productId);
            updateStockStmt.executeUpdate();

            System.out.println("Sale item deleted successfully!");
            return true;

        } catch (SQLException e) {
            System.out.println("Error deleting sale item: " + e.getMessage());
            return false;
        }
    }

    public static boolean confirmSaleGroup(int saleGroupId) {
        String sql = "UPDATE Sales SET status = 'Confirmed' WHERE saleGroupId = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, saleGroupId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Sale group confirmed successfully!");
                return true;
            } else {
                System.out.println("Error: Sale group not found!");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error confirming sale group: " + e.getMessage());
            return false;
        }
    }

    private static double getProductPrice(int productId) throws SQLException {
        String sql = "SELECT price FROM Products WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("price");
        }
        return 0;
    }
}