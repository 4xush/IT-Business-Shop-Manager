package tests;

import services.ReportService;
import models.Report;

public class ReportServiceTest {
    public static void main(String[] args) {
        System.out.println("=== Testing ReportService ===");

        ReportService reportService = new ReportService();

        // 1️⃣ Test daily report generation
        System.out.println("\nGenerating daily report...");
        Report dailyReport = reportService.generateReport("daily");
        displayReport(dailyReport);

        // 2️⃣ Test weekly report generation
        System.out.println("\nGenerating weekly report...");
        Report weeklyReport = reportService.generateReport("weekly");
        displayReport(weeklyReport);

        // 3️⃣ Test monthly report generation
        System.out.println("\nGenerating monthly report...");
        Report monthlyReport = reportService.generateReport("monthly");
        displayReport(monthlyReport);

        // 4️⃣ Test invalid period handling
        System.out.println("\nTesting invalid period input...");
        try {
            reportService.generateReport("yearly");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n=== ReportService Test Completed ===");
    }

    private static void displayReport(Report report) {
        System.out.println("Report Period: " + report.getStartTime() + " to " + report.getEndTime());
        System.out.println("Total Sales: " + report.getTotalSales());
        System.out.println("Total Sales Amount: ₹" + report.getTotalSalesAmount());
        System.out.println("Total Repairs Completed: " + report.getTotalRepairsCompleted());
        System.out.println("Total Repair Revenue: ₹" + report.getTotalRepairRevenue());
        System.out.println("Total Recharges Completed: " + report.getTotalRechargesCompleted());
        System.out.println("Total Recharge Amount: ₹" + report.getTotalRechargeAmount());
        System.out.println("Low Stock Products: " + report.getLowStockProducts());
    }
}
