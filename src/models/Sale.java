package models;

import java.sql.Timestamp;

public class Sale {
    private int id;
    private int productId;
    private int quantity;
    private double totalAmount;
    private Timestamp saleTime;

    public Sale(int id, int productId, int quantity, double totalAmount, Timestamp saleTime) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.saleTime = saleTime;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public Timestamp getSaleTime() { return saleTime; }
    public void setSaleTime(Timestamp saleTime) { this.saleTime = saleTime; }
}
