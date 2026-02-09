package com.monitor.dao;

import com.monitor.model.Note;
import com.monitor.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAOImpl implements NoteDAO {
    
    @Override
    public boolean createNote(Note note) throws SQLException {
        String sql = "INSERT INTO notes (user_id, title, content) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, note.getUserId());
            pstmt.setString(2, note.getTitle());
            pstmt.setString(3, note.getContent());
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Note> getAllNotes(int userId) throws SQLException {
        String sql = "SELECT * FROM notes WHERE user_id = ? ORDER BY updated_at DESC";
        return getNotes(sql, userId);
    }
    
    @Override
    public Note getNoteById(int noteId) throws SQLException {
        String sql = "SELECT * FROM notes WHERE note_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, noteId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return extractNote(rs);
        }
        return null;
    }
    
    @Override
    public boolean updateNote(Note note) throws SQLException {
        String sql = "UPDATE notes SET title = ?, content = ?, updated_at = CURRENT_TIMESTAMP WHERE note_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, note.getTitle());
            pstmt.setString(2, note.getContent());
            pstmt.setInt(3, note.getNoteId());
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean deleteNote(int noteId) throws SQLException {
        String sql = "DELETE FROM notes WHERE note_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, noteId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<Note> searchNotes(int userId, String keyword) throws SQLException {
        String sql = "SELECT * FROM notes WHERE user_id = ? AND (title LIKE ? OR content LIKE ?) ORDER BY updated_at DESC";
        List<Note> notes = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) notes.add(extractNote(rs));
        }
        return notes;
    }
    
    @Override
    public List<Note> getRecentNotes(int userId, int limit) throws SQLException {
        String sql = "SELECT * FROM notes WHERE user_id = ? ORDER BY updated_at DESC FETCH FIRST ? ROWS ONLY";
        List<Note> notes = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) notes.add(extractNote(rs));
        }
        return notes;
    }
    
    private List<Note> getNotes(String sql, int userId) throws SQLException {
        List<Note> notes = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) notes.add(extractNote(rs));
        }
        return notes;
    }
    
    private Note extractNote(ResultSet rs) throws SQLException {
        Note note = new Note();
        note.setNoteId(rs.getInt("note_id"));
        note.setUserId(rs.getInt("user_id"));
        note.setTitle(rs.getString("title"));
        note.setContent(rs.getString("content"));
        note.setCreatedAt(rs.getTimestamp("created_at"));
        note.setUpdatedAt(rs.getTimestamp("updated_at"));
        return note;
    }
}