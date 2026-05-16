package com.restaurant.dao;

import com.restaurant.entity.Cart;
import java.util.List;
import java.util.Map;

public interface OrderDAO {

    // Save order to database
    boolean saveOrder(Cart cart, int customerId, String paymentMethod, int tableNumber);

    // Get all orders for a customer
    List<Map<String, Object>> getOrdersByCustomerId(int customerId);
}