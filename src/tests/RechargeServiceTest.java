package tests;

import models.Recharge;
import services.RechargeService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class RechargeServiceTest {
    public static void main(String[] args) {
        try {
            // Connect to the test database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:kingcom.db");
            RechargeService rechargeService = new RechargeService(conn);

            // 1️⃣ Test Adding a Recharge Request
            Recharge testRecharge = new Recharge("9876543210", "Jio", 299.0);
            rechargeService.addRecharge(testRecharge);
            System.out.println("✅ Test 1 Passed: Recharge request added.");

            // 2️⃣ Test Fetching All Recharges
            List<Recharge> recharges = rechargeService.getAllRecharges();
            if (!recharges.isEmpty()) {
                System.out.println("✅ Test 2 Passed: Fetched recharge records.");
            } else {
                System.out.println("❌ Test 2 Failed: No recharges found.");
            }

            // 3️⃣ Test Updating Recharge Status
            if (!recharges.isEmpty()) {
                int rechargeId = recharges.get(0).getId(); // Get the first recharge ID
                rechargeService.updateRechargeStatus(rechargeId, "Successful");
                System.out.println("✅ Test 3 Passed: Recharge status updated.");
            } else {
                System.out.println("❌ Test 3 Failed: No recharge to update.");
            }

            // Close connection
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Database connection failed.");
        }
    }
}
