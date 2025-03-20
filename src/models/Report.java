package models;

import java.time.LocalDateTime;

public class Report {
    private int totalSales;
    private double totalSalesAmount;
    private int totalRepairsCompleted;
    private double totalRepairRevenue;
    private int totalRechargesCompleted;
    private double totalRechargeAmount;
    private int lowStockProducts; // Products with stock < 5
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Report(int totalSales, double totalSalesAmount, int totalRepairsCompleted, double totalRepairRevenue,
                  int totalRechargesCompleted, double totalRechargeAmount, int lowStockProducts,
                  LocalDateTime startTime, LocalDateTime endTime) {
        this.totalSales = totalSales;
        this.totalSalesAmount = totalSalesAmount;
        this.totalRepairsCompleted = totalRepairsCompleted;
        this.totalRepairRevenue = totalRepairRevenue;
        this.totalRechargesCompleted = totalRechargesCompleted;
        this.totalRechargeAmount = totalRechargeAmount;
        this.lowStockProducts = lowStockProducts;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public int getTotalSales() { return totalSales; }
    public double getTotalSalesAmount() { return totalSalesAmount; }
    public int getTotalRepairsCompleted() { return totalRepairsCompleted; }
    public double getTotalRepairRevenue() { return totalRepairRevenue; }
    public int getTotalRechargesCompleted() { return totalRechargesCompleted; }
    public double getTotalRechargeAmount() { return totalRechargeAmount; }
    public int getLowStockProducts() { return lowStockProducts; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}