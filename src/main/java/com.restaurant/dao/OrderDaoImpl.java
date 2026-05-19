/**
 * OrderDaoImpl.java
 *
 * JDBC implementation of OrderDAO — persists cart checkout rows to the orders
 * table and loads a customer's order history for the web UI.
 */
package com.restaurant.dao; // DAO package for database access classes

// Import classes needed for database and data storage
import com.restaurant.entity.Cart; // Shopping cart aggregate passed in at checkout
import com.restaurant.entity.Cart.CartItem; // Single line item inside the cart
import com.restaurant.utils.DBConnection; // Shared helper to open a MySQL connection
import java.sql.Connection; // JDBC connection handle
import java.sql.PreparedStatement; // Parameterized SQL for safe inserts and selects
import java.sql.ResultSet; // Cursor over rows returned by a SELECT
import java.sql.SQLException; // Checked exception when SQL or connectivity fails
import java.util.ArrayList; // Growable list for multiple order rows
import java.util.HashMap; // Key-value map for one order row shown on JSP
import java.util.List; // Interface type for returning order collections
import java.util.Map; // Generic map used as a lightweight row DTO

// This class does the actual database work for orders
public class OrderDaoImpl implements OrderDAO { // Concrete DAO wired by servlets

    // ============================================================
    // METHOD 1: Save order to database
    // ============================================================
    @Override // Fulfills OrderDAO contract
    public boolean saveOrder(Cart cart, int customerId, String paymentMethod, int tableNumber) { // Writes one DB row per cart line

        // Check if cart has any items. If empty, nothing to save.
        if (cart == null || cart.getItems().isEmpty()) { // Guard against null or empty checkout
            return false;  // No items, save failed
        }

        // Create variables for database connection
        Connection conn = null;           // Manages connection to database
        PreparedStatement statement = null;  // Holds our SQL query with ? placeholders

        try { // Main path: connect, insert each line, return success
            //  Connect to the database
            // DBConnection.getConnection() gives us a connection using settings from DBConnection.java
            conn = DBConnection.getConnection(); // Borrow a connection from the pool/helper

            //  Write the SQL query
            // The ? are placeholders (blanks to fill later)
            String sql = "INSERT INTO orders (customer_id, menu_id, menu_name, quantity, price, total_amount, payment_method, status, table_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; // One row per cart item

            //  Prepare the statement (like opening a form with blanks)
            statement = conn.prepareStatement(sql); // Compile INSERT once, reuse in the loop

            //  Loop through each item in cart and save it
            for (CartItem item : cart.getItems()) { // Each cart line becomes its own order row

                // Calculate total price for this one item (price × quantity)
                // for example Pizza Rs.20 × 2 = Rs.40
                double itemTotal = item.getPrice() * item.getQuantity(); // Line total before insert

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
            return true; // All line inserts completed without throwing

        } catch (SQLException e) { // Driver or SQL rejected the batch
            // If something went wrong (like database down or SQL error)
            System.out.println("Error saving order: " + e.getMessage()); // Log for server console debugging
            return false;  // Save failed

        } finally { // Always release JDBC resources
            // Close connection (ALWAYS do this to prevent memory leaks)
            try { // Nested try so close failures do not mask the original outcome
                if (statement != null) statement.close();  // Close the statement
                if (conn != null) conn.close();            // Close the connection
            } catch (SQLException e) { // Close itself can fail
                System.out.println("Error closing connection: " + e.getMessage()); // Log cleanup issue
            }
        }
    } // end saveOrder

    // ============================================================
    // METHOD 2: Get all orders for a specific customer
    // ============================================================
    @Override // Fulfills OrderDAO contract
    public List<Map<String, Object>> getOrdersByCustomerId(int customerId) { // History for "my orders" page

        // List to store all orders (will be sent to JSP)
        List<Map<String, Object>> orders = new ArrayList<>(); // Empty list filled in the loop below

        // Database connection objects
        Connection conn = null; // Connection for this read
        PreparedStatement statement = null; // SELECT with customer_id filter
        ResultSet rs = null;  // ResultSet holds the data returned from database

        try { // Connect, query, map rows into maps
            //  Connect to database
            conn = DBConnection.getConnection(); // Open connection for SELECT

            //  SQL query to get orders for this customer
            // ORDER BY order_id DESC means newest orders come first
            String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY order_id DESC"; // Newest first

            // Step 3: Prepare the statement
            statement = conn.prepareStatement(sql); // Bind customer id once
            statement.setInt(1, customerId);  // Fill ? with customer ID

            //  Execute query and get results
            rs = statement.executeQuery();  // This runs the SELECT query

            //  Loop through each row in the result
            while (rs.next()) { // One iteration per order line in the database

                // Create a Map to store one order row
                // Map is like a dictionary: key → value
                Map<String, Object> order = new HashMap<>(); // JSP-friendly row object

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
                orders.add(order); // Append mapped row to result list
            }

        } catch (SQLException e) { // Query or mapping failed
            // Print error if something goes wrong
            System.out.println("Error fetching orders: " + e.getMessage()); // Log and return partial/empty list
        } finally { // Release JDBC objects even when SELECT fails
            // Close everything (clean up)
            try { // Best-effort cleanup
                if (rs != null) rs.close();        // Close ResultSet
                if (statement != null) statement.close();  // Close statement
                if (conn != null) conn.close();    // Close connection
            } catch (SQLException e) { // Ignore close errors after logging
                System.out.println("Error closing connection: " + e.getMessage()); // Log cleanup issue
            }
        }

        // Return the list of orders (empty list if no orders found)
        return orders; // Caller renders list on JSP (may be empty)
    } // end getOrdersByCustomerId
} // end OrderDaoImpl
