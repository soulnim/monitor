package com.monitor.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Goal entity class representing user goals
 */
public class Goal {
    private int goalId;
    private int userId;
    private Integer categoryId;
    private String title;
    private String description;
    private Date targetDate;
    private String status; // ACTIVE, ACHIEVED, ABANDONED
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional field for category name
    private String categoryName;
    
    // Additional field for completion percentage
    private int completionPercentage;

    // Default constructor
    public Goal() {
    }

    // Constructor with essential fields
    public Goal(int userId, String title, String status) {
        this.userId = userId;
        this.title = title;
        this.status = status;
    }

    // Getters and Setters
    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "goalId=" + goalId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", completionPercentage=" + completionPercentage +
                '}';
    }
}