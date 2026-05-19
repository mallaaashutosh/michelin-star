/*
 * Contract for saving new orders at checkout and loading a customer's past orders.
 * Implementations handle JDBC and map results for the order history screens.
 */
package com.restaurant.dao; // groups order-related data access interfaces

import com.restaurant.entity.Cart; // cart contents and totals sent when placing an order
import java.util.List; // used when returning multiple order rows
import java.util.Map; // flexible row shape for order lists in the UI

public interface OrderDAO { // implemented by the class that talks to the orders tables

    // Save order to database
    boolean saveOrder(Cart cart, int customerId, String paymentMethod, int tableNumber);

    // Get all orders for a customer
    List<Map<String, Object>> getOrdersByCustomerId(int customerId);
}
