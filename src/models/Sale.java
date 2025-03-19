package models;

import java.sql.Timestamp;

public class Sale {
    private int id;
    private int saleGroupId; // Groups products into one sale
    private int productId;
    private int quantity;
    private double totalAmount; // Amount for this product (quantity * price)
    private Timestamp saleTime;
    private String status; // Pending, Confirmed

    public Sale(int id, int saleGroupId, int productId, int quantity, double totalAmount, Timestamp saleTime, String status) {
        this.id = id;
        this.saleGroupId = saleGroupId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.saleTime = saleTime;
        this.status = status;
    }

    // Partial constructor for new sale items
    public Sale(int saleGroupId, int productId, int quantity) {
        this.saleGroupId = saleGroupId;
        this.productId = productId;
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

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public Timestamp getSaleTime() { return saleTime; }
    public void setSaleTime(Timestamp saleTime) { this.saleTime = saleTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}