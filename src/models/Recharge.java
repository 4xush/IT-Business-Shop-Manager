package models;

import java.time.LocalDateTime;

public class Recharge {
    private int id;
    private String customerMobile;
    private String operator;
    private double amount;
    private String status; // Pending, Successful, Failed
    private LocalDateTime requestTime;

    public Recharge(int id, String customerMobile, String operator, double amount, String status, LocalDateTime requestTime) {
        this.id = id;
        this.customerMobile = customerMobile;
        this.operator = operator;
        this.amount = amount;
        this.status = status;
        this.requestTime = requestTime;
    }

    public Recharge(String customerMobile, String operator, double amount) {
        this.customerMobile = customerMobile;
        this.operator = operator;
        this.amount = amount;
        this.status = "Pending";
        this.requestTime = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomerMobile() { return customerMobile; }
    public void setCustomerMobile(String customerMobile) { this.customerMobile = customerMobile; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRequestTime() { return requestTime; }
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = requestTime; }
}
