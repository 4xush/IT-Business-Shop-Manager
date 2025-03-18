package services;

import models.Recharge;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RechargeService {
    private Connection conn;

    public RechargeService(Connection conn) {
        this.conn = conn;
    }

    public void addRecharge(Recharge recharge) {
        String sql = "INSERT INTO recharges (customer_mobile, operator, amount, status, request_time) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recharge.getCustomerMobile());
            stmt.setString(2, recharge.getOperator());
            stmt.setDouble(3, recharge.getAmount());
            stmt.setString(4, "Pending");
            stmt.setTimestamp(5, Timestamp.valueOf(recharge.getRequestTime()));
            stmt.executeUpdate();
            System.out.println("Recharge request added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRechargeStatus(int rechargeId, String status) {
        String sql = "UPDATE recharges SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, rechargeId);
            stmt.executeUpdate();
            System.out.println("Recharge status updated to " + status);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recharge> getAllRecharges() {
        List<Recharge> recharges = new ArrayList<>();
        String sql = "SELECT * FROM recharges";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                recharges.add(new Recharge(
                    rs.getInt("id"),
                    rs.getString("customer_mobile"),
                    rs.getString("operator"),
                    rs.getDouble("amount"),
                    rs.getString("status"),
                    rs.getTimestamp("request_time").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recharges;
    }
}
