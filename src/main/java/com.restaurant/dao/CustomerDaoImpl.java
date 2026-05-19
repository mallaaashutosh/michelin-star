/**
 * CustomerDaoImpl.java
 *
 * JDBC implementation of CustomerDAO — registers customers, authenticates
 * logins, and supports admin user approval workflows.
 */
package com.restaurant.dao; // DAO package for database access classes

import com.restaurant.entity.User; // Domain model mapped from the user table
import com.restaurant.utils.DBConnection; // Shared helper to open a MySQL connection
import com.restaurant.utils.PasswordHasher; // BCrypt verify for stored password hashes
import java.sql.Connection; // JDBC connection handle
import java.sql.PreparedStatement; // Parameterized SQL for inserts, updates, and selects
import java.sql.ResultSet; // Cursor over user rows
import java.sql.SQLException; // Checked exception propagated to servlets
import java.util.ArrayList; // Growable list for admin user listing
import java.util.List; // Interface return type for findAllUsers

public class CustomerDaoImpl implements CustomerDAO { // Concrete DAO for auth and user admin

    @Override // Fulfills CustomerDAO contract
    public boolean registerUser(User user) throws SQLException { // Sign-up inserts a pending customer row
        String query = "INSERT INTO user (name, phone_number, email, password, role, status) VALUES (?, ?, ?, ?, 'customer', 'pending')"; // New accounts need admin approval
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) { // Auto-close JDBC resources
            ps.setString(1, user.getName()); // Display name from registration form
            ps.setString(2, user.getPhoneNumber()); // Contact number
            ps.setString(3, user.getEmail()); // Login identifier; must be unique
            ps.setString(4, user.getPassword()); // Plain or pre-hashed password from service layer
            return ps.executeUpdate() > 0; // True when one row was inserted
        }
    }

    @Override // Fulfills CustomerDAO contract
    public User loginUser(String email, String password) throws SQLException { // Returns User on success, null otherwise
        String query = "SELECT * FROM user WHERE email = ?"; // Look up by email only
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email); // Bind email from login form
            try (ResultSet rs = ps.executeQuery()) { // At most one row per unique email
                if (rs.next()) { // User exists with this email
                    String stored = rs.getString("password"); // Hash or legacy plain text in DB
                    if (matchesPassword(password, stored)) { // Constant-time BCrypt or legacy fallback
                        return mapUser(rs); // Build entity without password field exposure elsewhere
                    }
                }
            }
        }
        return null; // Wrong email, wrong password, or no row
    }

    @Override // Fulfills CustomerDAO contract
    public boolean isEmailExists(String email) throws SQLException { // Used before register to avoid duplicates
        String query = "SELECT user_id FROM user WHERE email = ?"; // Lightweight existence check
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // True if any row matches
            }
        }
    }

    @Override // Fulfills CustomerDAO contract
    public List<User> findAllUsers() throws SQLException { // Admin page: list every account
        List<User> users = new ArrayList<>(); // Accumulator for mapped entities
        String query = "SELECT * FROM user ORDER BY created_at DESC"; // Newest registrations first
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { // One User per table row
                users.add(mapUser(rs)); // Copy columns into entity (no password in mapUser)
            }
        }
        return users; // May be empty when no users exist
    }

    @Override // Fulfills CustomerDAO contract
    public boolean updateUserStatus(int userId, String status) throws SQLException { // Approve or reject pending sign-ups
        String query = "UPDATE user SET status = ? WHERE user_id = ?"; // status e.g. active / rejected
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status); // New status value from admin action
            ps.setInt(2, userId); // Which account to update
            return ps.executeUpdate() > 0; // True when exactly one row changed
        }
    }

    private boolean matchesPassword(String plain, String stored) { // Supports BCrypt hashes and old plain passwords
        if (PasswordHasher.checkPassword(plain, stored)) { // Preferred path for hashed passwords
            return true;
        }
        return plain.equals(stored); // Legacy rows stored before hashing was introduced
    }

    private User mapUser(ResultSet rs) throws SQLException { // Shared row → entity mapping for reads
        User user = new User(); // Empty entity filled from current ResultSet row
        user.setId(rs.getInt("user_id")); // Primary key
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setRole(rs.getString("role")); // customer or admin
        user.setStatus(rs.getString("status")); // pending, active, etc.
        user.setCreatedAt(rs.getTimestamp("created_at")); // Audit timestamp
        user.setUpdatedAt(rs.getTimestamp("updated_at")); // Last change timestamp
        return user; // Ready for servlet / JSP
    }
} // end CustomerDaoImpl
