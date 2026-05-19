/*
 * Customer account persistence: sign-up, login, email checks, user lists, and status updates.
 * Keeps user-table access behind one interface so servlets stay thin.
 */
package com.restaurant.dao; // customer and user-account data access contracts

import com.restaurant.entity.User; // maps rows in the users table
import java.sql.SQLException; // thrown when a query or update fails
import java.util.List; // returned when loading every registered user

public interface CustomerDAO { // implemented by the JDBC repository for the users table

    boolean registerUser(User user) throws SQLException; // inserts a new customer record

    User loginUser(String email, String password) throws SQLException; // finds a user if credentials match

    boolean isEmailExists(String email) throws SQLException; // true when sign-up would duplicate an email

    List<User> findAllUsers() throws SQLException; // every user row for admin management screens

    boolean updateUserStatus(int userId, String status) throws SQLException; // activates or blocks an account by id
}
