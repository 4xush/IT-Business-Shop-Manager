package services;

import database.DatabaseConnection;
import models.Report;
import java.util.logging.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReportService {
    private int lowStockThreshold = 5;
    private static final Logger logger = Logger.getLogger(ReportService.class.getName()); // Add logger

    public Report generateReport(String period) {
        try (Connection conn = DatabaseConnection.connect()) {
            LocalDateTime[] timeRange = calculateTimeRange(period);
            Timestamp start = Timestamp.valueOf(timeRange[0]);
            Timestamp end = Timestamp.valueOf(timeRange[1]);

            // Combined metrics in single queries
            Object[] salesData = executeCombinedQuery(conn,
                "SELECT COUNT(*), COALESCE(SUM(totalAmount), 0) " +
                "FROM Sales WHERE status = 'Confirmed' AND saleTime BETWEEN ? AND ?",
                start, end
            );
            
            Object[] repairsData = executeCombinedQuery(conn,
                "SELECT COUNT(*), COALESCE(SUM(estimatedCost), 0) " +
                "FROM Repairs WHERE status = 'Completed' AND createdAt BETWEEN ? AND ?",
                start, end
            );
            
            Object[] rechargesData = executeCombinedQuery(conn,
                "SELECT COUNT(*), COALESCE(SUM(amount), 0) " +
                "FROM recharges WHERE status = 'Completed' AND request_time BETWEEN ? AND ?",
                start, end
            );

            int lowStockProducts = getLowStockProducts(conn);

            return new Report(
                (int) salesData[0],               // totalSales
                (double) salesData[1],             // totalSalesAmount
                (int) repairsData[0],              // totalRepairsCompleted
                (double) repairsData[1],           // totalRepairRevenue
                (int) rechargesData[0],            // totalRechargesCompleted
                (double) rechargesData[1],         // totalRechargeAmount
                lowStockProducts,
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

    private Object[] executeCombinedQuery(Connection conn, String sql, Timestamp start, Timestamp end) 
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, start);
            stmt.setTimestamp(2, end);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Object[]{
                    rs.getInt(1),
                    rs.getDouble(2)
                };
            }
            return new Object[]{0, 0.0};
        }
    }

    private int getLowStockProducts(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Products WHERE stock < ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lowStockThreshold);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // -- Optional: Setter for threshold --
    public void setLowStockThreshold(int threshold) {
        this.lowStockThreshold = threshold;
    }
}