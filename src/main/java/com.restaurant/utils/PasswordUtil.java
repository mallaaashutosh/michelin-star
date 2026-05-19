/*
 * Wraps BCrypt so we can hash passwords on signup and verify them at login.
 * Uses a cost factor of 10, which is a reasonable balance of speed and security for a class project.
 * Call getHashPassword when storing a new password, and checkPassword when someone logs in.
 */
package com.restaurant.utils; // utils package for security helpers

import org.mindrot.jbcrypt.BCrypt; // third-party library for salted password hashing

public class PasswordUtil { // static helpers only; no instances needed

    private static final int COST = 10; // BCrypt work factor — higher means slower but harder to crack

    public static String getHashPassword(String inputPassword) { // hash a plain-text password for storage
        String salt = BCrypt.gensalt(COST); // random salt unique to this password
        return BCrypt.hashpw(inputPassword, salt); // return the salted hash string to save in the DB
    }

    public static boolean checkPassword(String passwordTyped, String hashedPassword) { // compare login attempt to stored hash
        return BCrypt.checkpw(passwordTyped, hashedPassword); // true if the typed password matches
    }
}
