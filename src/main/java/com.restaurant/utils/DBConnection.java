/*
 * Central place to open JDBC connections to our MySQL restaurant database.
 * Servlets and DAO classes call getConnection() when they need to run SQL.
 * Connection settings (URL, user, password) live here so we don't repeat them everywhere.
 */
package com.restaurant.utils; // utils package for shared helpers

import java.sql.Connection; // JDBC connection type returned to callers
import java.sql.DriverManager; // creates the actual database connection
import java.sql.SQLException; // thrown when DB access fails

public class DBConnection { // helper class; not meant to be instantiated
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant"; // MySQL URL for local dev DB
    private static final String USER = "root"; // database username
    private static final String PASSWORD = ""; // empty password for local MySQL setup

    public static Connection getConnection() throws SQLException { // factory method used across the app
        try { // attempt to load driver before connecting
            Class.forName("com.mysql.cj.jdbc.Driver"); // load MySQL driver so JDBC can talk to MySQL
        } catch (ClassNotFoundException e) { // driver jar missing from classpath
            e.printStackTrace(); // print stack trace if driver jar is missing
        }
        return DriverManager.getConnection(URL, USER, PASSWORD); // open and return a live connection
    }
}
