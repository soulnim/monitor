package com.monitor.dao;

import com.monitor.model.Note;
import java.sql.SQLException;
import java.util.List;

public interface NoteDAO {
    boolean createNote(Note note) throws SQLException;
    List<Note> getAllNotes(int userId) throws SQLException;
    Note getNoteById(int noteId) throws SQLException;
    boolean updateNote(Note note) throws SQLException;
    boolean deleteNote(int noteId) throws SQLException;
    List<Note> searchNotes(int userId, String keyword) throws SQLException;
    List<Note> getRecentNotes(int userId, int limit) throws SQLException;
}