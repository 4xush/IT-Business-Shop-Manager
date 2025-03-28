package services;

import models.Recharge;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RechargeService {

    public boolean addRecharge(Recharge recharge) {
        String sql = "INSERT INTO recharges (customerMobile, operator, amount, status, requestTime) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recharge.getCustomerMobile());
            stmt.setString(2, recharge.getOperator());
            stmt.setDouble(3, recharge.getAmount());
            stmt.setString(4, recharge.getStatus()); // Use object’s status
            stmt.setTimestamp(5, Timestamp.valueOf(recharge.getRequestTime()));
            stmt.executeUpdate();
            System.out.println("Recharge request added successfully.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRechargeStatus(int rechargeId, String status) {
        String sql = "UPDATE recharges SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, rechargeId);
            stmt.executeUpdate();
            System.out.println("Recharge status updated to " + status);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Recharge> getAllRecharges() {
        List<Recharge> recharges = new ArrayList<>();
        String sql = "SELECT * FROM recharges";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recharges.add(new Recharge(
                    rs.getInt("id"),
                    rs.getString("customerMobile"),
                    rs.getString("operator"),
                    rs.getDouble("amount"),
                    rs.getString("status"),
                    rs.getTimestamp("requestTime").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recharges;
    }
}