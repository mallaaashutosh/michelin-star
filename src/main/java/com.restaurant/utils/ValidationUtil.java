/*
 * Shared input checks for registration and profile forms.
 * Validates email format and enforces a strong-password policy before we hit the database.
 * Also exposes a friendly message listing what the password must include.
 */
package com.restaurant.utils; // utils package for form validation

import java.util.regex.Pattern; // used to match email and password against rules

public class ValidationUtil { // static validators only
    
    // Email validation regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$" // basic pattern: local part @ domain
    );
    
    // Password validation requirements:
    // - At least 8 characters
    // - At least one uppercase letter
    // - At least one lowercase letter
    // - At least one number
    // - At least one special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$" // regex enforcing all rules above
    );
    
    /**
     * Validates email format
     * @param email The email to validate
     * @return true if email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) { // reject missing or blank email
            return false; // not valid
        }
        return EMAIL_PATTERN.matcher(email).matches(); // true when it looks like a real email
    }
    
    /**
     * Validates password strength
     * @param password The password to validate
     * @return true if password meets strength requirements, false otherwise
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.isEmpty()) { // reject missing or blank password
            return false; // not strong enough
        }
        return PASSWORD_PATTERN.matcher(password).matches(); // true when it passes the strength regex
    }
    
    /**
     * Returns detailed password requirement message
     * @return String describing password requirements
     */
    public static String getPasswordRequirements() {
        return "Password must contain: 8+ characters, uppercase, lowercase, number, and special character (@$!%*?&)"; // shown on failed validation
    }
}
