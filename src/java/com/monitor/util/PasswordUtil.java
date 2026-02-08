package com.monitor.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Password utility class for hashing and verifying passwords
 * Uses SHA-256 with salt (simplified version)
 * 
 * Note: For production, use BCrypt library (jBCrypt)
 * Add this dependency to your project: org.mindrot:jbcrypt:0.4
 */
public class PasswordUtil {
    
    private static final int SALT_LENGTH = 16;
    
    /**
     * Generate a random salt
     * @return Base64 encoded salt
     */
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hash password with salt using SHA-256
     * @param password Plain text password
     * @param salt Salt string
     * @return Hashed password
     */
    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Hash a password (creates salt and hashes)
     * Format: salt:hash
     * @param password Plain text password
     * @return Salted hash in format "salt:hash"
     */
    public static String hashPassword(String password) {
        String salt = generateSalt();
        String hash = hashPassword(password, salt);
        return salt + ":" + hash;
    }
    
    /**
     * Verify a password against a stored hash
     * @param password Plain text password to verify
     * @param storedHash Stored hash in format "salt:hash"
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            String salt = parts[0];
            String hash = parts[1];
            
            String computedHash = hashPassword(password, salt);
            return hash.equals(computedHash);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Validate password strength
     * Password must be at least 8 characters with uppercase, lowercase, and number
     * @param password Password to validate
     * @return true if password is strong enough
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasUpper && hasLower && hasDigit;
    }
    
    /**
     * Get password strength message
     * @param password Password to check
     * @return Error message or null if valid
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password is required";
        }
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }
        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one number";
        }
        return null; // Valid password
    }
}