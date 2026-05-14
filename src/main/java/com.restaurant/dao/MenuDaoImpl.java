package com.restaurant.dao;

import com.restaurant.entity.MenuItem;
import com.restaurant.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuDaoImpl implements MenuDAO {

    // add new menu item to database
    @Override
    public boolean insertMenuItem(MenuItem menuItem) {
        String query = "INSERT INTO menu (name, category, price, availability) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, menuItem.getName());
            ps.setString(2, menuItem.getCategory());
            ps.setDouble(3, menuItem.getPrice());
            ps.setString(4, menuItem.getAvailability());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting menu item: " + e.getMessage());
            return false;
        }
    }

    // get all menu items
    @Override
    public ArrayList<MenuItem> fetchAllMenuItems() {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menu";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("menu_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("availability")
                );
                menuItems.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching menu items: " + e.getMessage());
        }
        return menuItems;
    }

    // find one menu item by its id
    @Override
    public MenuItem findMenuItemById(int menuId) {
        String query = "SELECT * FROM menu WHERE menu_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, menuId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new MenuItem(
                            rs.getInt("menu_id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getString("availability")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding menu item: " + e.getMessage());
        }
        return null;
    }

    // update menu item
    @Override
    public boolean updateMenuItem(MenuItem menuItem) {
        String query = "UPDATE menu SET name = ?, category = ?, price = ?, availability = ? WHERE menu_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, menuItem.getName());
            ps.setString(2, menuItem.getCategory());
            ps.setDouble(3, menuItem.getPrice());
            ps.setString(4, menuItem.getAvailability());
            ps.setInt(5, menuItem.getMenuId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating menu item: " + e.getMessage());
            return false;
        }
    }

    // delete menu item
    @Override
    public boolean deleteMenuItem(int menuId) {
        String query = "DELETE FROM menu WHERE menu_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, menuId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting menu item: " + e.getMessage());
            return false;
        }
    }

    // get menu items by category (like Japanese, Nepali)
    @Override
    public ArrayList<MenuItem> fetchMenuItemsByCategory(String category) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menu WHERE category = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = new MenuItem(
                            rs.getInt("menu_id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getString("availability")
                    );
                    menuItems.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching by category: " + e.getMessage());
        }
        return menuItems;
    }

    // search menu items by name
    @Override
    public ArrayList<MenuItem> searchMenuItemsByName(String keyword) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menu WHERE LOWER(name) LIKE LOWER(?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MenuItem item = new MenuItem(
                            rs.getInt("menu_id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getString("availability")
                    );
                    menuItems.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching menu: " + e.getMessage());
        }
        return menuItems;
    }
}