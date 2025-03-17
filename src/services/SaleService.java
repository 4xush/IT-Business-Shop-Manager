package services;

import database.DatabaseConnection;
import models.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleService {

    // Add a new Sale (Handles Stock Validation)
    public static void addSale(int productId, int quantity) {
        String stockCheckSQL = "SELECT stock, price FROM Products WHERE id = ?";
        String insertSaleSQL = "INSERT INTO viewTables();Sales (productId, quantity, totalAmount, saleTime) VALUES (?, ?, ?, ?)";
        String updateStockSQL = "UPDATE Products SET stock = stock - ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stockCheckStmt = conn.prepareStatement(stockCheckSQL);
             PreparedStatement insertSaleStmt = conn.prepareStatement(insertSaleSQL);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL)) {

            // Check stock availability
            stockCheckStmt.setInt(1, productId);
            ResultSet rs = stockCheckStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Product not found!");
                return;
            }

            int availableStock = rs.getInt("stock");
            double price = rs.getDouble("price");

            if (availableStock < quantity) {
                System.out.println("Error: Not enough stock available!");
                return;
            }

            double totalAmount = quantity * price;
            Timestamp saleTime = new Timestamp(System.currentTimeMillis());

            // Insert Sale
            insertSaleStmt.setInt(1, productId);
            insertSaleStmt.setInt(2, quantity);
            insertSaleStmt.setDouble(3, totalAmount);
            insertSaleStmt.setTimestamp(4, saleTime);
            insertSaleStmt.executeUpdate();

            // Update Product Stock
            updateStockStmt.setInt(1, quantity);
            updateStockStmt.setInt(2, productId);
            updateStockStmt.executeUpdate();

            System.out.println("Sale recorded successfully!");

        } catch (SQLException e) {
            System.out.println("Error processing sale: " + e.getMessage());
        }
    }

    // Get All Sales
    public static List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM Sales";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sales.add(new Sale(
                        rs.getInt("id"),
                        rs.getInt("productId"),
                        rs.getInt("quantity"),
                        rs.getDouble("totalAmount"),
                        rs.getTimestamp("saleTime")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching sales: " + e.getMessage());
        }
        return sales;
    }

    // Update Sale (Edge Case: Cannot reduce quantity below previous stock levels)
    public static void updateSale(int saleId, int newQuantity) {
        String getSaleSQL = "SELECT productId, quantity FROM Sales WHERE id = ?";
        String updateSaleSQL = "UPDATE Sales SET quantity = ?, totalAmount = ? WHERE id = ?";
        String updateStockSQL = "UPDATE Products SET stock = stock + ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement getSaleStmt = conn.prepareStatement(getSaleSQL);
             PreparedStatement updateSaleStmt = conn.prepareStatement(updateSaleSQL);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL)) {

            // Fetch Sale Data
            getSaleStmt.setInt(1, saleId);
            ResultSet rs = getSaleStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Sale not found!");
                return;
            }

            int productId = rs.getInt("productId");
            int oldQuantity = rs.getInt("quantity");

            // Stock Adjustment (Add back old quantity, subtract new quantity)
            int quantityDifference = oldQuantity - newQuantity;

            // Update Stock
            updateStockStmt.setInt(1, quantityDifference);
            updateStockStmt.setInt(2, productId);
            updateStockStmt.executeUpdate();

            // Update Sale
            double newTotalAmount = newQuantity * getProductPrice(productId);
            updateSaleStmt.setInt(1, newQuantity);
            updateSaleStmt.setDouble(2, newTotalAmount);
            updateSaleStmt.setInt(3, saleId);
            updateSaleStmt.executeUpdate();

            System.out.println("Sale updated successfully!");

        } catch (SQLException e) {
            System.out.println("Error updating sale: " + e.getMessage());
        }
    }

    // Delete Sale (Handles Stock Reversal)
    public static void deleteSale(int saleId) {
        String getSaleSQL = "SELECT productId, quantity FROM Sales WHERE id = ?";
        String deleteSaleSQL = "DELETE FROM Sales WHERE id = ?";
        String updateStockSQL = "UPDATE Products SET stock = stock + ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement getSaleStmt = conn.prepareStatement(getSaleSQL);
             PreparedStatement deleteSaleStmt = conn.prepareStatement(deleteSaleSQL);
             PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL)) {

            // Fetch Sale Data
            getSaleStmt.setInt(1, saleId);
            ResultSet rs = getSaleStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Sale not found!");
                return;
            }

            int productId = rs.getInt("productId");
            int quantity = rs.getInt("quantity");

            // Delete Sale
            deleteSaleStmt.setInt(1, saleId);
            deleteSaleStmt.executeUpdate();

            // Revert Stock
            updateStockStmt.setInt(1, quantity);
            updateStockStmt.setInt(2, productId);
            updateStockStmt.executeUpdate();

            System.out.println("Sale deleted successfully!");

        } catch (SQLException e) {
            System.out.println("Error deleting sale: " + e.getMessage());
        }
    }

    // Helper Function: Get Product Price
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
