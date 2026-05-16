package com.restaurant.dao;

// Import classes needed for database and data storage
import com.restaurant.entity.Cart;
import com.restaurant.entity.Cart.CartItem;
import com.restaurant.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This class does the actual database work for orders
public class OrderDaoImpl implements OrderDAO {

    // ============================================================
    // METHOD 1: Save order to database
    // ============================================================
    @Override
    public boolean saveOrder(Cart cart, int customerId, String paymentMethod, int tableNumber) {

        // Check if cart has any items. If empty, nothing to save.
        if (cart == null || cart.getItems().isEmpty()) {
            return false;  // No items, save failed
        }

        // Create variables for database connection
        Connection conn = null;           // Manages connection to database
        PreparedStatement statement = null;  // Holds our SQL query with ? placeholders

        try {
            //  Connect to the database
            // DBConnection.getConnection() gives us a connection using settings from DBConnection.java
            conn = DBConnection.getConnection();

            //  Write the SQL query
            // The ? are placeholders (blanks to fill later)
            String sql = "INSERT INTO orders (customer_id, menu_id, menu_name, quantity, price, total_amount, payment_method, status, table_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            //  Prepare the statement (like opening a form with blanks)
            statement = conn.prepareStatement(sql);

            //  Loop through each item in cart and save it
            for (CartItem item : cart.getItems()) {

                // Calculate total price for this one item (price × quantity)
                // for example Pizza Rs.20 × 2 = Rs.40
                double itemTotal = item.getPrice() * item.getQuantity();

                // Fill the ? placeholders with actual values
                statement.setInt(1, customerId);           // Fill ? 1: Customer ID (who ordered)
                statement.setInt(2, item.getMenuId());     // Fill ? 2: Menu item ID
                statement.setString(3, item.getName());    // Fill ? 3: Item name (Pizza, Burger, etc)
                statement.setInt(4, item.getQuantity());   // Fill ? 4: How many of this item
                statement.setDouble(5, item.getPrice());   // Fill ? 5: Price of one item
                statement.setDouble(6, itemTotal);         // Fill ? 6: Total for this item
                statement.setString(7, paymentMethod);     // Fill ? 7: Cash, Card, or Online
                statement.setString(8, "pending");         // Fill ? 8: Status (pending means not delivered yet)
                statement.setInt(9, tableNumber);          // Fill ? 9: Table number where customer sits

                //  Execute (send to database)
                statement.executeUpdate();  // This actually saves the data to MySQL
            }

            // If all items saved successfully, return true
            return true;

        } catch (SQLException e) {
            // If something went wrong (like database down or SQL error)
            System.out.println("Error saving order: " + e.getMessage());
            return false;  // Save failed

        } finally {
            // Close connection (ALWAYS do this to prevent memory leaks)
            try {
                if (statement != null) statement.close();  // Close the statement
                if (conn != null) conn.close();            // Close the connection
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // ============================================================
    // METHOD 2: Get all orders for a specific customer
    // ============================================================
    @Override
    public List<Map<String, Object>> getOrdersByCustomerId(int customerId) {

        // List to store all orders (will be sent to JSP)
        List<Map<String, Object>> orders = new ArrayList<>();

        // Database connection objects
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;  // ResultSet holds the data returned from database

        try {
            //  Connect to database
            conn = DBConnection.getConnection();

            //  SQL query to get orders for this customer
            // ORDER BY order_id DESC means newest orders come first
            String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY order_id DESC";

            // Step 3: Prepare the statement
            statement = conn.prepareStatement(sql);
            statement.setInt(1, customerId);  // Fill ? with customer ID

            //  Execute query and get results
            rs = statement.executeQuery();  // This runs the SELECT query

            //  Loop through each row in the result
            while (rs.next()) {

                // Create a Map to store one order row
                // Map is like a dictionary: key → value
                Map<String, Object> order = new HashMap<>();

                // Put each column value into the map
                order.put("order_id", rs.getInt("order_id"));           // Order ID number
                order.put("menu_name", rs.getString("menu_name"));       // Name of food item
                order.put("quantity", rs.getInt("quantity"));            // How many ordered
                order.put("price", rs.getDouble("price"));               // Price per item
                order.put("total_amount", rs.getDouble("total_amount")); // Total for this item
                order.put("payment_method", rs.getString("payment_method")); // Cash/Card/Online
                order.put("status", rs.getString("status"));             // pending or completed
                order.put("table_number", rs.getInt("table_number"));    // Table number
                order.put("created_at", rs.getTimestamp("created_at"));  // Date and time of order

                // Add this order to the list
                orders.add(order);
            }

        } catch (SQLException e) {
            // Print error if something goes wrong
            System.out.println("Error fetching orders: " + e.getMessage());
        } finally {
            // Close everything (clean up)
            try {
                if (rs != null) rs.close();        // Close ResultSet
                if (statement != null) statement.close();  // Close statement
                if (conn != null) conn.close();    // Close connection
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }

        // Return the list of orders (empty list if no orders found)
        return orders;
    }
}