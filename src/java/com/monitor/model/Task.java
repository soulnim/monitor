package com.monitor.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Task entity class representing user tasks
 */
public class Task {
    private int taskId;
    private int userId;
    private String title;
    private String description;
    private Date dueDate;
    private String priority; // HIGH, MEDIUM, LOW
    private String status; // PENDING, IN_PROGRESS, COMPLETED
    private Timestamp completedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Task() {
    }

    // Constructor with essential fields
    public Task(int userId, String title, String priority, String status) {
        this.userId = userId;
        this.title = title;
        this.priority = priority;
        this.status = status;
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", dueDate=" + dueDate +
                '}';
    }
}