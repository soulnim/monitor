package com.monitor.controller;

import com.monitor.dao.*;
import com.monitor.model.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "NoteController", urlPatterns = {"/notes", "/notes/add", "/notes/edit", "/notes/delete"})
public class NoteController extends HttpServlet {
    private NoteDAO noteDAO;
    
    @Override
    public void init() { noteDAO = new NoteDAOImpl(); }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String path = request.getServletPath();
        if (path.equals("/notes/add")) {
            request.getRequestDispatcher("/views/notes/add-note.jsp").forward(request, response);
        } else if (path.equals("/notes/edit")) {
            showEditNote(request, response);
        } else {
            showNotes(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String path = request.getServletPath();
        if (path.equals("/notes/add")) {
            handleAddNote(request, response);
        } else if (path.equals("/notes/edit")) {
            handleEditNote(request, response);
        } else if (path.equals("/notes/delete")) {
            handleDeleteNote(request, response);
        }
    }
    
    private void showNotes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        try {
            String search = request.getParameter("search");
            List<Note> notes;
            if (search != null && !search.isEmpty()) {
                notes = noteDAO.searchNotes(userId, search);
            } else {
                notes = noteDAO.getAllNotes(userId);
            }
            request.setAttribute("notes", notes);
            request.getRequestDispatcher("/views/notes/notes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading notes");
            request.getRequestDispatcher("/views/notes/notes.jsp").forward(request, response);
        }
    }
    
    private void showEditNote(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int noteId = Integer.parseInt(request.getParameter("id"));
            Note note = noteDAO.getNoteById(noteId);
            request.setAttribute("note", note);
            request.getRequestDispatcher("/views/notes/edit-note.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/notes?error=view");
        }
    }
    
    private void handleAddNote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = (Integer) request.getSession().getAttribute("userId");
        try {
            Note note = new Note();
            note.setUserId(userId);
            note.setTitle(request.getParameter("title"));
            note.setContent(request.getParameter("content"));
            noteDAO.createNote(note);
            response.sendRedirect(request.getContextPath() + "/notes?success=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/notes?error=add");
        }
    }
    
    private void handleEditNote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Note note = new Note();
            note.setNoteId(Integer.parseInt(request.getParameter("noteId")));
            note.setTitle(request.getParameter("title"));
            note.setContent(request.getParameter("content"));
            noteDAO.updateNote(note);
            response.sendRedirect(request.getContextPath() + "/notes?updated=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/notes?error=update");
        }
    }
    
    private void handleDeleteNote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int noteId = Integer.parseInt(request.getParameter("noteId"));
            noteDAO.deleteNote(noteId);
            response.sendRedirect(request.getContextPath() + "/notes?deleted=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/notes?error=delete");
        }
    }
}