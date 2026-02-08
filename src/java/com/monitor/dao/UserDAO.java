package com.monitor.dao;

import com.monitor.model.User;
import java.sql.SQLException;

/**
 * Data Access Object interface for User operations
 * Defines contract for user-related database operations
 */
public interface UserDAO {
    
    /**
     * Register a new user
     * @param user User object to register
     * @return true if registration successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean registerUser(User user) throws SQLException;
    
    /**
     * Find user by email
     * @param email Email address
     * @return User object if found, null otherwise
     * @throws SQLException if database error occurs
     */
    User findByEmail(String email) throws SQLException;
    
    /**
     * Find user by username
     * @param username Username
     * @return User object if found, null otherwise
     * @throws SQLException if database error occurs
     */
    User findByUsername(String username) throws SQLException;
    
    /**
     * Find user by ID
     * @param userId User ID
     * @return User object if found, null otherwise
     * @throws SQLException if database error occurs
     */
    User findById(int userId) throws SQLException;
    
    /**
     * Validate user credentials for login
     * @param email Email or username
     * @param password Plain text password
     * @return User object if credentials valid, null otherwise
     * @throws SQLException if database error occurs
     */
    User validateUser(String email, String password) throws SQLException;
    
    /**
     * Update user profile
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean updateUser(User user) throws SQLException;
    
    /**
     * Update user password
     * @param userId User ID
     * @param newPasswordHash New hashed password
     * @return true if update successful, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean updatePassword(int userId, String newPasswordHash) throws SQLException;
    
    /**
     * Check if email already exists
     * @param email Email to check
     * @return true if email exists, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean emailExists(String email) throws SQLException;
    
    /**
     * Check if username already exists
     * @param username Username to check
     * @return true if username exists, false otherwise
     * @throws SQLException if database error occurs
     */
    boolean usernameExists(String username) throws SQLException;
}