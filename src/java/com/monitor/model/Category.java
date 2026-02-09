package com.monitor.model;

import java.sql.Timestamp;

/**
 * Category entity class for transactions and goals
 */
public class Category {
    private int categoryId;
    private int userId;
    private String categoryName;
    private String categoryType; // INCOME, EXPENSE, GOAL
    private boolean isDefault; // true for system categories, false for user-created
    private Timestamp createdAt;

    // Default constructor
    public Category() {
    }

    // Constructor with essential fields
    public Category(int userId, String categoryName, String categoryType) {
        this.userId = userId;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
        this.isDefault = false;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryType='" + categoryType + '\'' +
                '}';
    }
}