package services;

import database.DatabaseConnection;
import models.Repair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepairService {

    public static boolean addRepair(String customerName, String phoneNumber, String deviceModel, 
                                    String issueDescription, double estimatedCost) {
        String sql = "INSERT INTO Repairs (customerName, phoneNumber, deviceModel, issueDescription, estimatedCost, status, createdAt) " +
                     "VALUES (?, ?, ?, ?, ?, 'Pending', ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            stmt.setString(1, customerName);
            stmt.setString(2, phoneNumber);
            stmt.setString(3, deviceModel);
            stmt.setString(4, issueDescription);
            stmt.setDouble(5, estimatedCost);
            stmt.setTimestamp(6, createdAt);
            stmt.executeUpdate();
            System.out.println("Repair request added successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding repair request: " + e.getMessage());
            return false;
        }
    }

    public static List<Repair> getAllRepairs() {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT * FROM Repairs";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                repairs.add(new Repair(
                        rs.getInt("id"),
                        rs.getString("customerName"),
                        rs.getString("phoneNumber"),
                        rs.getString("deviceModel"),
                        rs.getString("issueDescription"),
                        rs.getDouble("estimatedCost"),
                        rs.getString("status"),
                        rs.getTimestamp("createdAt")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching repairs: " + e.getMessage());
        }
        return repairs;
    }

    public static boolean updateRepairStatus(int repairId, String newStatus) {
        String checkStatusSQL = "SELECT status FROM Repairs WHERE id = ?";
        String updateStatusSQL = "UPDATE Repairs SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement checkStatusStmt = conn.prepareStatement(checkStatusSQL);
             PreparedStatement updateStatusStmt = conn.prepareStatement(updateStatusSQL)) {
            checkStatusStmt.setInt(1, repairId);
            ResultSet rs = checkStatusStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Repair request not found!");
                return false;
            }
            String currentStatus = rs.getString("status");
            if (currentStatus.equals("Completed")) {
                System.out.println("Error: Cannot update status of a completed repair!");
                return false;
            }
            updateStatusStmt.setString(1, newStatus);
            updateStatusStmt.setInt(2, repairId);
            updateStatusStmt.executeUpdate();
            System.out.println("Repair status updated successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating repair status: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteRepair(int repairId) {
        String checkStatusSQL = "SELECT status FROM Repairs WHERE id = ?";
        String deleteSQL = "DELETE FROM Repairs WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement checkStatusStmt = conn.prepareStatement(checkStatusSQL);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
            checkStatusStmt.setInt(1, repairId);
            ResultSet rs = checkStatusStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Error: Repair request not found!");
                return false;
            }
            String status = rs.getString("status");
            if (status.equals("Completed")) {
                System.out.println("Error: Cannot delete a completed repair request!");
                return false;
            }
            deleteStmt.setInt(1, repairId);
            deleteStmt.executeUpdate();
            System.out.println("Repair request deleted successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting repair request: " + e.getMessage());
            return false;
        }
    }
}