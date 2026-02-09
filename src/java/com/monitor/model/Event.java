package com.monitor.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Event entity class for calendar/schedule
 */
public class Event {
    private int eventId;
    private int userId;
    private String title;
    private String description;
    private Date eventDate;
    private Time startTime;
    private Time endTime;
    private String eventType;
    private boolean isRecurring;
    private String recurringFrequency; // DAILY, WEEKLY, MONTHLY
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Event() {
    }

    // Constructor with essential fields
    public Event(int userId, String title, Date eventDate) {
        this.userId = userId;
        this.title = title;
        this.eventDate = eventDate;
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
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

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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
        return "Event{" +
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", eventDate=" + eventDate +
                ", startTime=" + startTime +
                '}';
    }
}