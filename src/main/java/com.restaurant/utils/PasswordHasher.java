/*
 * Alternative password hashing using SHA-256 and Base64 encoding.
 * Simpler than BCrypt but less ideal for production; useful where we need a lightweight hash.
 * checkPassword re-hashes the input and compares it to the stored hash string.
 */
package com.restaurant.utils; // utils package for hashing helpers
import java.security.MessageDigest; // API for one-way hash algorithms like SHA-256
import java.security.NoSuchAlgorithmException; // thrown if SHA-256 is not available on this JVM
import java.util.Base64; // encodes raw hash bytes into a safe text string


public class PasswordHasher { // static utility for SHA-256 password hashing
    public static String hashPassword(String password) { // turn a plain password into a Base64 hash
        try { // SHA-256 can throw if algo name is wrong
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // get the SHA-256 digest instance
            byte[] hashedBytes = md.digest(password.getBytes()); // run the hash on the password bytes
            return Base64.getEncoder().encodeToString(hashedBytes); // store as readable text in the DB
        } catch (NoSuchAlgorithmException e) { // JVM does not support SHA-256 (very unlikely)
            throw new RuntimeException("Error hashing password", e); // wrap in unchecked exception if algo missing
        }
    }

    public static boolean checkPassword(String password, String hashed) { // verify login by comparing hashes
        return hashPassword(password).equals(hashed); // true when re-hash matches what we saved
    }

}
