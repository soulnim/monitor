package com.monitor.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility for Java DB (Derby)
 * Provides database connection management
 */
public class DBConnection {
    
    // Database credentials - MODIFY THESE ACCORDING TO YOUR SETUP
    private static final String DB_URL = "jdbc:derby://localhost:1527/monitordb";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "app";
    private static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";
    
    // Static block to load driver
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("Derby JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("Derby JDBC Driver not found");
            e.printStackTrace();
        }
    }
    
    /**
     * Get database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to create database connection");
            throw e;
        }
    }
    
    /**
     * Close database connection
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed");
            e.printStackTrace();
            return false;
        }
    }
}