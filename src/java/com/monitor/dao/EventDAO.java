package com.monitor.dao;

import com.monitor.model.Event;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface EventDAO {
    boolean createEvent(Event event) throws SQLException;
    List<Event> getAllEvents(int userId) throws SQLException;
    List<Event> getEventsByDate(int userId, Date date) throws SQLException;
    List<Event> getUpcomingEvents(int userId, int days) throws SQLException;
    Event getEventById(int eventId) throws SQLException;
    boolean updateEvent(Event event) throws SQLException;
    boolean deleteEvent(int eventId) throws SQLException;
}