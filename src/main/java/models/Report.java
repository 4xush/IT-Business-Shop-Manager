package models;

import java.time.LocalDateTime;
import java.util.List;
import services.ReportService.SaleItem;
import services.ReportService.RepairItem;
import services.ReportService.RechargeItem;
import services.ReportService.ProductItem;

public class Report {
    private final int totalSales;
    private final double totalSalesAmount;
    private final List<SaleItem> salesItems;
    private final int totalRepairsCompleted;
    private final double totalRepairRevenue;
    private final List<RepairItem> repairItems;
    private final int totalRechargesCompleted;
    private final double totalRechargeAmount;
    private final List<RechargeItem> rechargeItems;
    private final int lowStockProducts;
    private final List<ProductItem> lowStockItems;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public Report(int totalSales, double totalSalesAmount, List<SaleItem> salesItems,
                  int totalRepairsCompleted, double totalRepairRevenue, List<RepairItem> repairItems,
                  int totalRechargesCompleted, double totalRechargeAmount, List<RechargeItem> rechargeItems,
                  int lowStockProducts, List<ProductItem> lowStockItems,
                  LocalDateTime startTime, LocalDateTime endTime) {
        this.totalSales = totalSales;
        this.totalSalesAmount = totalSalesAmount;
        this.salesItems = salesItems;
        this.totalRepairsCompleted = totalRepairsCompleted;
        this.totalRepairRevenue = totalRepairRevenue;
        this.repairItems = repairItems;
        this.totalRechargesCompleted = totalRechargesCompleted;
        this.totalRechargeAmount = totalRechargeAmount;
        this.rechargeItems = rechargeItems;
        this.lowStockProducts = lowStockProducts;
        this.lowStockItems = lowStockItems;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public int getTotalSales() { return totalSales; }
    public double getTotalSalesAmount() { return totalSalesAmount; }
    public List<SaleItem> getSalesItems() { return salesItems; }
    public int getTotalRepairsCompleted() { return totalRepairsCompleted; }
    public double getTotalRepairRevenue() { return totalRepairRevenue; }
    public List<RepairItem> getRepairItems() { return repairItems; }
    public int getTotalRechargesCompleted() { return totalRechargesCompleted; }
    public double getTotalRechargeAmount() { return totalRechargeAmount; }
    public List<RechargeItem> getRechargeItems() { return rechargeItems; }
    public int getLowStockProducts() { return lowStockProducts; }
    public List<ProductItem> getLowStockItems() { return lowStockItems; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}