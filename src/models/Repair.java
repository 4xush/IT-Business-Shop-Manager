package models;

import java.sql.Timestamp;

public class Repair {
    private int id;
    private String customerName;
    private String phoneNumber;
    private String deviceModel;
    private String issueDescription;
    private double estimatedCost;
    private String status; // Pending, In Progress, Completed
    private Timestamp createdAt;

    public Repair(int id, String customerName, String phoneNumber, String deviceModel, 
                  String issueDescription, double estimatedCost, String status, Timestamp createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.deviceModel = deviceModel;
        this.issueDescription = issueDescription;
        this.estimatedCost = estimatedCost;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }

    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    public double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
