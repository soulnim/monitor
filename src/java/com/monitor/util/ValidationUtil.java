package com.monitor.util;

import java.util.regex.Pattern;

/**
 * Validation utility class for input validation
 * Prevents XSS and validates user inputs
 */
public class ValidationUtil {
    
    // Email regex pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    // Username pattern (alphanumeric, underscore, hyphen, 3-50 chars)
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_-]{3,50}$");
    
    /**
     * Validate email format
     * @param email Email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate username format
     * @param username Username to validate
     * @return true if valid username format
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }
    
    /**
     * Check if string is null or empty
     * @param str String to check
     * @return true if null or empty/whitespace
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Sanitize string to prevent XSS
     * Escapes HTML special characters
     * @param input Input string
     * @return Sanitized string
     */
    public static String sanitizeHtml(String input) {
        if (input == null) {
            return null;
        }
        
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;")
                   .replace("/", "&#x2F;");
    }
    
    /**
     * Validate string length
     * @param str String to validate
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @return true if length is within range
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validate numeric input
     * @param str String to validate
     * @return true if string is a valid number
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate positive number
     * @param str String to validate
     * @return true if string is a positive number
     */
    public static boolean isPositiveNumber(String str) {
        if (!isNumeric(str)) {
            return false;
        }
        try {
            double value = Double.parseDouble(str.trim());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate date format (yyyy-MM-dd)
     * @param dateStr Date string to validate
     * @return true if valid date format
     */
    public static boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        // Simple regex for yyyy-MM-dd format
        return dateStr.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }
}