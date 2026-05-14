package com.restaurant.dao;

import com.restaurant.entity.User;
import java.sql.SQLException;

public interface UserDAO {

    boolean registerUser(User user) throws SQLException;

    User loginUser(String email, String password) throws SQLException;

    boolean isEmailExists(String email) throws SQLException;
}