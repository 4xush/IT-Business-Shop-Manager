package tests;

import services.RepairService;
import models.Repair;

import java.util.List;

public class RepairServiceTest {
    public static void main(String[] args) {
        System.out.println("=== Testing RepairService ===");

        // 1️⃣ Add a new repair request
        System.out.println("\nAdding a new repair request...");
        RepairService.addRepair("John Doe", "9876543210", "iPhone 12", 
                                "Screen cracked", 120.50);

        // 2️⃣ Get all repairs and display
        System.out.println("\nFetching all repairs...");
        List<Repair> repairs = RepairService.getAllRepairs();
        if (repairs.isEmpty()) {
            System.out.println("No repairs found.");
        } else {
            for (Repair repair : repairs) {
                System.out.println(repair.getId() + " | " + repair.getCustomerName() + 
                                   " | " + repair.getDeviceModel() + " | " + repair.getStatus());
            }
        }

        // 3️⃣ Update status of a repair
        if (!repairs.isEmpty()) {
            int repairId = repairs.get(0).getId();
            System.out.println("\nUpdating repair status to 'In Progress' for ID: " + repairId);
            RepairService.updateRepairStatus(repairId, "In Progress");

            System.out.println("\nUpdating repair status to 'Completed' for ID: " + repairId);
            RepairService.updateRepairStatus(repairId, "Completed");

            // 4️⃣ Try updating again (should fail)
            System.out.println("\nAttempting to update status after completion (should fail)...");
            RepairService.updateRepairStatus(repairId, "Pending");
        } else {
            System.out.println("\nNo repairs to update.");
        }

        // 5️⃣ Try deleting a completed repair (should fail)
        if (!repairs.isEmpty()) {
            int repairId = repairs.get(0).getId();
            System.out.println("\nAttempting to delete a completed repair (should fail)...");
            RepairService.deleteRepair(repairId);
        }

        // 6️⃣ Add a new repair and delete it before completion (should work)
        System.out.println("\nAdding another repair for deletion...");
        RepairService.addRepair("Jane Smith", "9123456789", "Samsung Galaxy S21", 
                                "Battery issue", 75.00);

        System.out.println("\nFetching all repairs...");
        repairs = RepairService.getAllRepairs();
        if (repairs.isEmpty()) {
            System.out.println("No repairs found.");
        } else {
            for (Repair repair : repairs) {
                System.out.println(repair.getId() + " | " + repair.getCustomerName() + 
                                   " | " + repair.getDeviceModel() + " | " + repair.getStatus());
            }

            // Get the last added repair's ID
            int newRepairId = repairs.get(repairs.size() - 1).getId();
            System.out.println("\nDeleting the repair request for ID: " + newRepairId);
            RepairService.deleteRepair(newRepairId);
        }

        System.out.println("\n=== RepairService Test Completed ===");
    }
}