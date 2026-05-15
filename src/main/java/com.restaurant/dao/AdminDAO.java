package com.restaurant.dao;

import java.sql.SQLException;
import java.util.Map;

public interface AdminDAO {

    Map<String, Integer> getDashboardStats() throws SQLException;
}
