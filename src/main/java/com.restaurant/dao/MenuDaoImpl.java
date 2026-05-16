package com.restaurant.dao;


import com.restaurant.entity.MenuItem;// Import the MenuItem class (the blueprint for food items)
import com.restaurant.utils.DBConnection;// Import database connection helper
import java.sql.Connection;// Import classes for database work
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// This class does all the database work for menu items
public class MenuDaoImpl implements MenuDAO {

    // ============================================================
    // ADD NEW FOOD ITEM TO DATABASE
    // ============================================================
    @Override
    public boolean insertMenuItem(MenuItem menuItem) {
        // SQL query with ? placeholders for values we will fill later
        String query = "INSERT INTO menu (name, category, price, image, availability) VALUES (?, ?, ?, ?, ?)";

        // Try-with-resources: connection and statement auto-close when done
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Fill the ? placeholders with actual values from menuItem object
            ps.setString(1, menuItem.getName());        // Fill first ? with food name
            ps.setString(2, menuItem.getCategory());    // Fill second ? with category (Nepali, Chinese, etc)
            ps.setDouble(3, menuItem.getPrice());       // Fill third ? with price
            ps.setString(4, menuItem.getImage());       // Fill fourth ? with image filename
            ps.setString(5, menuItem.getAvailability()); // Fill fifth ? with available or not

            // executeUpdate() runs the INSERT command
            // Returns true if at least one row was inserted
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // If something went wrong, print error message
            System.out.println("Error inserting menu item: " + e.getMessage());
            return false;  // Insert failed
        }
    }

    // ============================================================
    // GET ALL FOOD ITEMS FROM DATABASE
    // ============================================================
    @Override
    public ArrayList<MenuItem> fetchAllMenuItems() {
        // Create empty list to store all menu items
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        // SQL query to get everything from menu table
        String query = "SELECT * FROM menu";

        // Try-with-resources: auto closes connection, statement, and result set
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            // Loop through each row in the result
            while (rs.next()) {
                // Create a new MenuItem object using data from current row
                MenuItem item = new MenuItem(
                        rs.getInt("menu_id"),        // Get menu_id column value
                        rs.getString("name"),        // Get name column value
                        rs.getString("category"),    // Get category column value
                        rs.getDouble("price"),       // Get price column value
                        rs.getString("image"),       // Get image column value
                        rs.getString("availability") // Get availability column value
                );
                // Add this item to the list
                menuItems.add(item);
            }
        } catch (SQLException e) {
            // Print error if something went wrong
            System.out.println("Error fetching menu items: " + e.getMessage());
        }
        // Return the list (empty if no items found)
        return menuItems;
    }

    // ============================================================
    // FIND ONE FOOD ITEM BY ITS ID
    // ============================================================
    @Override
    public MenuItem findMenuItemById(int menuId) {
        // SQL query with ? for the menu_id
        String query = "SELECT * FROM menu WHERE menu_id = ?";

        // Try-with-resources: auto closes connection and statement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Fill the ? with the menuId we are looking for
            ps.setInt(1, menuId);

            // Execute query and get result
            try (ResultSet rs = ps.executeQuery()) {
                // If a row is found (rs.next() returns true)
                if (rs.next()) {
                    // Create and return a MenuItem object from the row data
                    return new MenuItem(
                            rs.getInt("menu_id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getString("image"),
                            rs.getString("availability")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding menu item: " + e.getMessage());
        }
        // Return null if no item found with that ID
        return null;
    }

    // ============================================================
    // UPDATE EXISTING FOOD ITEM
    // ============================================================
    @Override
    public boolean updateMenuItem(MenuItem menuItem) {
        // SQL query to update a row (all columns except menu_id)
        String query = "UPDATE menu SET name = ?, category = ?, price = ?, image = ?, availability = ? WHERE menu_id = ?";

        // Try-with-resources: auto closes connection and statement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Fill all the ? placeholders with new values
            ps.setString(1, menuItem.getName());        // New name
            ps.setString(2, menuItem.getCategory());    // New category
            ps.setDouble(3, menuItem.getPrice());       // New price
            ps.setString(4, menuItem.getImage());       // New image name
            ps.setString(5, menuItem.getAvailability()); // New availability
            ps.setInt(6, menuItem.getMenuId());         // Which item to update (WHERE clause)

            // executeUpdate() returns number of rows updated
            // Returns true if at least one row was updated
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating menu item: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // DELETE FOOD ITEM FROM DATABASE
    // ============================================================
    @Override
    public boolean deleteMenuItem(int menuId) {
        // SQL query to delete a row by its ID
        String query = "DELETE FROM menu WHERE menu_id = ?";

        // Try-with-resources: auto closes connection and statement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Fill the ? with the menuId of item to delete
            ps.setInt(1, menuId);

            // executeUpdate() returns number of rows deleted
            // Returns true if at least one row was deleted
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting menu item: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // GET FOOD ITEMS BY CATEGORY (like Nepali, Chinese, Indian)
    // ============================================================
    @Override
    public ArrayList<MenuItem> fetchMenuItemsByCategory(String category) {
        // Create empty list to store items
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        // SQL query to get items matching a category
        String query = "SELECT * FROM menu WHERE category = ?";

        // Try-with-resources: auto closes connection and statement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Fill the ? with the category name (e.g., "Nepali")
            ps.setString(1, category);

            // Execute query and get results
            try (ResultSet rs = ps.executeQuery()) {
                // Loop through each row
                while (rs.next()) {
                    // Create MenuItem object from row data
                    MenuItem item = new MenuItem(
                            rs.getInt("menu_id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getString("image"),
                            rs.getString("availability")
                    );
                    // Add to list
                    menuItems.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching by category: " + e.getMessage());
        }
        // Return list (empty if no items in this category)
        return menuItems;
    }

    // ============================================================
    // SEARCH FOOD ITEMS BY NAME
    // ============================================================
    @Override
    public ArrayList<MenuItem> searchMenuItemsByName(String keyword) {
        // Create empty list to store search results
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        // SQL query with LIKE to find matching names (case insensitive)
        String query = "SELECT * FROM menu WHERE LOWER(name) LIKE LOWER(?)";

        // Try-with-resources: auto closes connection and statement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Fill the ? with keyword wrapped in % (e.g., "%momo%")
            // % means "anything" - so it finds any name containing the keyword
            ps.setString(1, "%" + keyword + "%");

            // Execute query and get results
            try (ResultSet rs = ps.executeQuery()) {
                // Loop through each matching row
                while (rs.next()) {
                    // Create MenuItem object from row data
                    MenuItem item = new MenuItem(
                            rs.getInt("menu_id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getString("image"),
                            rs.getString("availability")
                    );
                    // Add to list
                    menuItems.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching menu: " + e.getMessage());
        }
        // Return list of matching items (empty if none found)
        return menuItems;
    }
}