package services;

import database.DatabaseConnection;
import models.Report;
import java.util.logging.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    private int lowStockThreshold = 5;
    private static final Logger logger = Logger.getLogger(ReportService.class.getName());

    public Report generateReport(String period) {
        try (Connection conn = DatabaseConnection.connect()) {
            LocalDateTime[] timeRange = calculateTimeRange(period);
            Timestamp start = Timestamp.valueOf(timeRange[0]);
            Timestamp end = Timestamp.valueOf(timeRange[1]);

            // Combined metrics with details
            Object[] salesData = getSalesData(conn, start, end);
            System.out.println("salesData  : " + salesData[0]);
            Object[] repairsData = getRepairsData(conn, start, end);
            Object[] rechargesData = getRechargesData(conn, start, end);
            List<ProductItem> lowStockItems = getLowStockItems(conn);

            return new Report(
                (int) salesData[0],                // totalSales
                (double) salesData[1],             // totalSalesAmount
                (List<SaleItem>) salesData[2],     // salesItems
                (int) repairsData[0],              // totalRepairsCompleted
                (double) repairsData[1],           // totalRepairRevenue
                (List<RepairItem>) repairsData[2], // repairItems
                (int) rechargesData[0],            // totalRechargesCompleted
                (double) rechargesData[1],         // totalRechargeAmount
                (List<RechargeItem>) rechargesData[2], // rechargeItems
                lowStockItems.size(),              // lowStockProducts count
                lowStockItems,                     // lowStockItems list
                timeRange[0],                      // startTime
                timeRange[1]                       // endTime
            );
        } catch (SQLException e) {
            logger.severe("Report generation failed: " + e.getMessage());
            throw new RuntimeException("Report generation error", e);
        }
    }

    // -- Helper Methods --

    private LocalDateTime[] calculateTimeRange(String period) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime;

        switch (period.toLowerCase()) {
            case "daily":
                startTime = endTime.truncatedTo(ChronoUnit.DAYS);
                break;
            case "weekly":
                startTime = endTime.minus(6, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
                break;
            case "monthly":
                startTime = endTime.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

        return new LocalDateTime[]{startTime, endTime};
    }

    private Object[] getSalesData(Connection conn, Timestamp start, Timestamp end) throws SQLException {
        String sql = "SELECT COUNT(*), COALESCE(SUM(totalAmount), 0) FROM Sales " +
                     "WHERE status = 'Confirmed' AND saleTime BETWEEN ? AND ?";
        String detailSql = "SELECT Id, totalAmount, saleTime FROM Sales " +
                          "WHERE status = 'Confirmed' AND saleTime BETWEEN ? AND ?";

        // Get counts and totals
        int totalSales;
        double totalSalesAmount;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, start);
            stmt.setTimestamp(2, end);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalSales = rs.getInt(1);
                totalSalesAmount = rs.getDouble(2);
            } else {
                totalSales = 0;
                totalSalesAmount = 0.0;
            }
        }

        // Get detailed sales items
        List<SaleItem> salesItems = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(detailSql)) {
            stmt.setTimestamp(1, start);
            stmt.setTimestamp(2, end);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                salesItems.add(new SaleItem(
                    rs.getInt("Id"),
                    rs.getDouble("totalAmount"),
                    rs.getTimestamp("saleTime").toLocalDateTime()
                ));
            }
        }

        return new Object[]{totalSales, totalSalesAmount, salesItems};
    }

    private Object[] getRepairsData(Connection conn, Timestamp start, Timestamp end) throws SQLException {
        String sql = "SELECT COUNT(*), COALESCE(SUM(estimatedCost), 0) FROM Repairs " +
                     "WHERE status = 'Completed' AND createdAt BETWEEN ? AND ?";
        String detailSql = "SELECT Id, estimatedCost, createdAt FROM Repairs " +
                          "WHERE status = 'Completed' AND createdAt BETWEEN ? AND ?";

        // Get counts and totals
        int totalRepairs;
        double totalRepairRevenue;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, start);
            stmt.setTimestamp(2, end);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalRepairs = rs.getInt(1);
                totalRepairRevenue = rs.getDouble(2);
            } else {
                totalRepairs = 0;
                totalRepairRevenue = 0.0;
            }
        }

        // Get detailed repair items
        List<RepairItem> repairItems = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(detailSql)) {
            stmt.setTimestamp(1, start);
            stmt.setTimestamp(2, end);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                repairItems.add(new RepairItem(
                    rs.getInt("Id"),
                    rs.getDouble("estimatedCost"),
                    rs.getTimestamp("createdAt").toLocalDateTime()
                ));
            }
        }

        return new Object[]{totalRepairs, totalRepairRevenue, repairItems};
    }

    private Object[] getRechargesData(Connection conn, Timestamp start, Timestamp end) throws SQLException {
        String sql = "SELECT COUNT(*), COALESCE(SUM(amount), 0) FROM recharges " +
                     "WHERE status = 'Successful' AND requestTime BETWEEN ? AND ?";
        String detailSql = "SELECT Id, amount, requestTime FROM recharges " +
                          "WHERE status = 'Successful' AND requestTime BETWEEN ? AND ?";

        // Get counts and totals
        int totalRecharges;
        double totalRechargeAmount;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, start);
            stmt.setTimestamp(2, end);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalRecharges = rs.getInt(1);
                totalRechargeAmount = rs.getDouble(2);
            } else {
                totalRecharges = 0;
                totalRechargeAmount = 0.0;
            }
        }

        // Get detailed recharge items
        List<RechargeItem> rechargeItems = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(detailSql)) {
            stmt.setTimestamp(1, start);
            stmt.setTimestamp(2, end);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rechargeItems.add(new RechargeItem(
                    rs.getInt("Id"),
                    rs.getDouble("amount"),
                    rs.getTimestamp("requestTime").toLocalDateTime()
                ));
            }
        }

        return new Object[]{totalRecharges, totalRechargeAmount, rechargeItems};
    }

    private List<ProductItem> getLowStockItems(Connection conn) throws SQLException {
        String sql = "SELECT Id, name, stock FROM Products WHERE stock < ?";
        List<ProductItem> lowStockItems = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lowStockThreshold);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lowStockItems.add(new ProductItem(
                    rs.getInt("Id"),
                    rs.getString("name"),
                    rs.getInt("stock")
                ));
            }
        }
        return lowStockItems;
    }

    // -- Inner Classes for Detailed Items --

    public static class SaleItem {
        private final int saleId;
        private final double totalAmount;
        private final LocalDateTime saleTime;

        public SaleItem(int saleId, double totalAmount, LocalDateTime saleTime) {
            this.saleId = saleId;
            this.totalAmount = totalAmount;
            this.saleTime = saleTime;
        }

        public int getSaleId() { return saleId; }
        public double getTotalAmount() { return totalAmount; }
        public LocalDateTime getSaleTime() { return saleTime; }
    }

    public static class RepairItem {
        private final int repairId;
        private final double estimatedCost;
        private final LocalDateTime createdAt;

        public RepairItem(int repairId, double estimatedCost, LocalDateTime createdAt) {
            this.repairId = repairId;
            this.estimatedCost = estimatedCost;
            this.createdAt = createdAt;
        }

        public int getRepairId() { return repairId; }
        public double getEstimatedCost() { return estimatedCost; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }

    public static class RechargeItem {
        private final int rechargeId;
        private final double amount;
        private final LocalDateTime requestTime;

        public RechargeItem(int rechargeId, double amount, LocalDateTime requestTime) {
            this.rechargeId = rechargeId;
            this.amount = amount;
            this.requestTime = requestTime;
        }

        public int getRechargeId() { return rechargeId; }
        public double getAmount() { return amount; }
        public LocalDateTime getRequestTime() { return requestTime; }
    }

    public static class ProductItem {
        private final int productId;
        private final String name;
        private final int stock;

        public ProductItem(int productId, String name, int stock) {
            this.productId = productId;
            this.name = name;
            this.stock = stock;
        }

        public int getProductId() { return productId; }
        public String getName() { return name; }
        public int getStock() { return stock; }
    }
}