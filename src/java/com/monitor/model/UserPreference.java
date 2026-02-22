package com.monitor.model;

public class UserPreference {
    private int userId;
    private boolean emailNotifications;
    private boolean billReminders;
    private boolean taskReminders;
    private String theme;
    private String language;
    private String dateFormat;
    private String currency;
    private String profileVisibility;
    private boolean dataSharing;

    public UserPreference() {
        this.theme = "dark";
        this.language = "en";
        this.dateFormat = "MM/DD/YYYY";
        this.currency = "USD";
        this.profileVisibility = "private";
        this.emailNotifications = true;
        this.billReminders = true;
        this.taskReminders = true;
        this.dataSharing = false;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public boolean isEmailNotifications() { return emailNotifications; }
    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications = emailNotifications; }
    public boolean isBillReminders() { return billReminders; }
    public void setBillReminders(boolean billReminders) { this.billReminders = billReminders; }
    public boolean isTaskReminders() { return taskReminders; }
    public void setTaskReminders(boolean taskReminders) { this.taskReminders = taskReminders; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getProfileVisibility() { return profileVisibility; }
    public void setProfileVisibility(String profileVisibility) { this.profileVisibility = profileVisibility; }
    public boolean isDataSharing() { return dataSharing; }
    public void setDataSharing(boolean dataSharing) { this.dataSharing = dataSharing; }
}
