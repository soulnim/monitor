package com.monitor.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Budget {
    private int budgetId;
    private int userId;
    private int categoryId;
    private String categoryName;
    private int budgetMonth;
    private int budgetYear;
    private BigDecimal budgetLimit;
    private BigDecimal spentAmount;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public int getBudgetId() { return budgetId; }
    public void setBudgetId(int budgetId) { this.budgetId = budgetId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public int getBudgetMonth() { return budgetMonth; }
    public void setBudgetMonth(int budgetMonth) { this.budgetMonth = budgetMonth; }
    public int getBudgetYear() { return budgetYear; }
    public void setBudgetYear(int budgetYear) { this.budgetYear = budgetYear; }
    public BigDecimal getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(BigDecimal budgetLimit) { this.budgetLimit = budgetLimit; }
    public BigDecimal getSpentAmount() { return spentAmount; }
    public void setSpentAmount(BigDecimal spentAmount) { this.spentAmount = spentAmount; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getUsagePercentage() {
        if (budgetLimit == null || budgetLimit.signum() <= 0 || spentAmount == null) {
            return 0;
        }
        return spentAmount.multiply(new BigDecimal("100")).divide(budgetLimit, 0, BigDecimal.ROUND_HALF_UP).intValue();
    }
}
