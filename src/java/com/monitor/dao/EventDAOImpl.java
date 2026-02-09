package com.monitor.dao;

import com.monitor.model.Event;
import com.monitor.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements EventDAO {
    
    @Override
    public boolean createEvent(Event event) throws SQLException {
        String sql = "INSERT INTO events (user_id, title, description, event_date, start_time, end_time, event_type, is_recurring, recurring_frequency) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, event.getUserId());
            pstmt.setString(2, event.getTitle());
            pstmt.setString(3, event.getDescription());
            pstmt.setDate(4, event.getEventDate());
            pstmt.setTime(5, event.getStartTime());
            pstmt.setTime(6, event.getEndTime());
            pstmt.setString(7, event.getEventType());
            pstmt.setInt(8, event.isRecurring() ? 1 : 0);
            pstmt.setString(9, event.getRecurringFrequency());
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Event> getAllEvents(int userId) throws SQLException {
        String sql = "SELECT * FROM events WHERE user_id = ? ORDER BY event_date, start_time";
        return getEvents(sql, userId);
    }
    
    @Override
    public List<Event> getEventsByDate(int userId, Date date) throws SQLException {
        String sql = "SELECT * FROM events WHERE user_id = ? AND event_date = ? ORDER BY start_time";
        List<Event> events = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) events.add(extractEvent(rs));
        }
        return events;
    }
    
    @Override
    public List<Event> getUpcomingEvents(int userId, int days) throws SQLException {
        String sql =
            "SELECT * FROM events " +
            "WHERE user_id = ? " +
            "AND event_date BETWEEN CURRENT_DATE " +
            "AND DATE({fn TIMESTAMPADD(SQL_TSI_DAY, ?, CURRENT_DATE)}) " +
            "ORDER BY event_date, start_time";

        List<Event> events = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, days);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                events.add(extractEvent(rs));
            }
        }
        return events;
    }
    
    @Override
    public Event getEventById(int eventId) throws SQLException {
        String sql = "SELECT * FROM events WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return extractEvent(rs);
        }
        return null;
    }
    
    @Override
    public boolean updateEvent(Event event) throws SQLException {
        String sql = "UPDATE events SET title = ?, description = ?, event_date = ?, start_time = ?, end_time = ?, event_type = ?, updated_at = CURRENT_TIMESTAMP WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setDate(3, event.getEventDate());
            pstmt.setTime(4, event.getStartTime());
            pstmt.setTime(5, event.getEndTime());
            pstmt.setString(6, event.getEventType());
            pstmt.setInt(7, event.getEventId());
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    private List<Event> getEvents(String sql, int userId) throws SQLException {
        List<Event> events = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) events.add(extractEvent(rs));
        }
        return events;
    }
    
    private Event extractEvent(ResultSet rs) throws SQLException {
        Event e = new Event();
        e.setEventId(rs.getInt("event_id"));
        e.setUserId(rs.getInt("user_id"));
        e.setTitle(rs.getString("title"));
        e.setDescription(rs.getString("description"));
        e.setEventDate(rs.getDate("event_date"));
        e.setStartTime(rs.getTime("start_time"));
        e.setEndTime(rs.getTime("end_time"));
        e.setEventType(rs.getString("event_type"));
        e.setRecurring(rs.getInt("is_recurring") == 1);
        e.setRecurringFrequency(rs.getString("recurring_frequency"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        e.setUpdatedAt(rs.getTimestamp("updated_at"));
        return e;
    }
}