package com.monitor.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Milestone entity class for goal milestones
 */
public class Milestone {
    private int milestoneId;
    private int goalId;
    private String title;
    private String description;
    private Date targetDate;
    private boolean isCompleted;
    private Timestamp completedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Milestone() {
    }

    // Constructor with essential fields
    public Milestone(int goalId, String title) {
        this.goalId = goalId;
        this.title = title;
        this.isCompleted = false;
    }

    // Getters and Setters
    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
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
        return "Milestone{" +
                "milestoneId=" + milestoneId +
                ", title='" + title + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}