package com.michelinstar.dao;

import com.michelinstar.model.User;
import com.michelinstar.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    
    public boolean registerUser(User user) throws SQLException {
        String query = "INSERT INTO customer (name, phone_number, email, password, profile_image) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPhoneNumber());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getProfileImage());
            
            return ps.executeUpdate() > 0;
        }
    }

    public User loginUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM customer WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashed = rs.getString("password");
                    if (com.michelinstar.util.PasswordHasher.checkPassword(password, hashed)) {
                        User user = new User();
                        user.setId(rs.getInt("customer_id"));
                        user.setName(rs.getString("name"));
                        user.setEmail(rs.getString("email"));
                        user.setPhoneNumber(rs.getString("phone_number"));
                        user.setProfileImage(rs.getString("profile_image"));
                        return user;
                    }
                }
            }
        }
        return null;
    }

    public boolean isEmailExists(String email) throws SQLException {
        String query = "SELECT customer_id FROM customer WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
