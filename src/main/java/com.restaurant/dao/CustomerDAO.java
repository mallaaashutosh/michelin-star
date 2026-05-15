package com.restaurant.dao;

import com.restaurant.entity.User;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO {

    boolean registerUser(User user) throws SQLException;

    User loginUser(String email, String password) throws SQLException;

    boolean isEmailExists(String email) throws SQLException;

    List<User> findAllUsers() throws SQLException;

    boolean updateUserStatus(int userId, String status) throws SQLException;
}