package com.monitor.dao;

import com.monitor.model.UserPreference;
import com.monitor.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPreferenceDAOImpl implements UserPreferenceDAO {

    @Override
    public void ensureTableExists() throws SQLException {
        String sql = "CREATE TABLE user_preferences ("
                + "user_id INTEGER NOT NULL, "
                + "email_notifications SMALLINT DEFAULT 1, "
                + "bill_reminders SMALLINT DEFAULT 1, "
                + "task_reminders SMALLINT DEFAULT 1, "
                + "theme VARCHAR(10) DEFAULT 'dark', "
                + "language VARCHAR(10) DEFAULT 'en', "
                + "date_format VARCHAR(20) DEFAULT 'MM/DD/YYYY', "
                + "currency VARCHAR(10) DEFAULT 'USD', "
                + "profile_visibility VARCHAR(20) DEFAULT 'private', "
                + "data_sharing SMALLINT DEFAULT 0, "
                + "CONSTRAINT pk_user_preferences PRIMARY KEY (user_id), "
                + "CONSTRAINT fk_user_preferences_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                throw e;
            }
        }
    }

    @Override
    public UserPreference getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM user_preferences WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UserPreference pref = new UserPreference();
                pref.setUserId(userId);
                pref.setEmailNotifications(rs.getInt("email_notifications") == 1);
                pref.setBillReminders(rs.getInt("bill_reminders") == 1);
                pref.setTaskReminders(rs.getInt("task_reminders") == 1);
                pref.setTheme(rs.getString("theme"));
                pref.setLanguage(rs.getString("language"));
                pref.setDateFormat(rs.getString("date_format"));
                pref.setCurrency(rs.getString("currency"));
                pref.setProfileVisibility(rs.getString("profile_visibility"));
                pref.setDataSharing(rs.getInt("data_sharing") == 1);
                return pref;
            }
        }
        UserPreference pref = new UserPreference();
        pref.setUserId(userId);
        return pref;
    }

    @Override
    public boolean saveOrUpdate(UserPreference preference) throws SQLException {
        String updateSql = "UPDATE user_preferences SET email_notifications = ?, bill_reminders = ?, task_reminders = ?, theme = ?, language = ?, date_format = ?, currency = ?, profile_visibility = ?, data_sharing = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            apply(pstmt, preference);
            pstmt.setInt(10, preference.getUserId());
            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                return true;
            }
        }

        String insertSql = "INSERT INTO user_preferences (email_notifications, bill_reminders, task_reminders, theme, language, date_format, currency, profile_visibility, data_sharing, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            apply(pstmt, preference);
            pstmt.setInt(10, preference.getUserId());
            return pstmt.executeUpdate() > 0;
        }
    }

    private void apply(PreparedStatement pstmt, UserPreference p) throws SQLException {
        pstmt.setInt(1, p.isEmailNotifications() ? 1 : 0);
        pstmt.setInt(2, p.isBillReminders() ? 1 : 0);
        pstmt.setInt(3, p.isTaskReminders() ? 1 : 0);
        pstmt.setString(4, p.getTheme());
        pstmt.setString(5, p.getLanguage());
        pstmt.setString(6, p.getDateFormat());
        pstmt.setString(7, p.getCurrency());
        pstmt.setString(8, p.getProfileVisibility());
        pstmt.setInt(9, p.isDataSharing() ? 1 : 0);
    }
}
