package com.monitor.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Bill entity class representing bills and payments
 */
public class Bill {
    private int billId;
    private int userId;
    private String billName;
    private BigDecimal amount;
    private Date dueDate;
    private String category;
    private String status; // UNPAID, PAID, OVERDUE
    private Date paymentDate;
    private boolean isRecurring;
    private String recurringFrequency; // MONTHLY, YEARLY, etc.
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Bill() {
    }

    // Constructor with essential fields
    public Bill(int userId, String billName, BigDecimal amount, Date dueDate, String category) {
        this.userId = userId;
        this.billName = billName;
        this.amount = amount;
        this.dueDate = dueDate;
        this.category = category;
        this.status = "UNPAID";
    }

    // Getters and Setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    public void setRecurringFrequency(String recurringFrequency) {
        this.recurringFrequency = recurringFrequency;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", billName='" + billName + '\'' +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }
}