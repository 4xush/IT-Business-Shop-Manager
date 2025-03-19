package models;

import java.sql.Timestamp;

public class Sale {
    private int id;
    private int saleGroupId;
    private int productId;
    private String productName; // Added for display
    private int quantity;
    private double totalAmount;
    private Timestamp saleTime;
    private String status;

    public Sale(int id, int saleGroupId, int productId, String productName, int quantity, double totalAmount, Timestamp saleTime, String status) {
        this.id = id;
        this.saleGroupId = saleGroupId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.saleTime = saleTime;
        this.status = status;
    }

    // Partial constructor for new sale items
    public Sale(int saleGroupId, int productId, int quantity) {
        this.saleGroupId = saleGroupId;
        this.productId = productId;
        this.productName = null; // Will be set when retrieved
        this.quantity = quantity;
        this.totalAmount = 0; // Calculated later
        this.saleTime = new Timestamp(System.currentTimeMillis());
        this.status = "Pending";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSaleGroupId() { return saleGroupId; }
    public void setSaleGroupId(int saleGroupId) { this.saleGroupId = saleGroupId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public Timestamp getSaleTime() { return saleTime; }
    public void setSaleTime(Timestamp saleTime) { this.saleTime = saleTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}