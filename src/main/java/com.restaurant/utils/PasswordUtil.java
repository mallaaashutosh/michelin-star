package com.restaurant.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private static final int COST = 10;

    public static String getHashPassword(String inputPassword) {
        String salt = BCrypt.gensalt(COST);
        return BCrypt.hashpw(inputPassword, salt);
    }

    public static boolean checkPassword(String passwordTyped, String hashedPassword) {
        return BCrypt.checkpw(passwordTyped, hashedPassword);
    }
}