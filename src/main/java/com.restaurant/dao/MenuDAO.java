package com.restaurant.dao;

import com.restaurant.entity.MenuItem;
import java.util.ArrayList;

// This interface lists all the operations I can do with menu items
// The actual code will be written in MenuDaoImpl.java
public interface MenuDAO {

    // Add a new food item to database
    boolean insertMenuItem(MenuItem menuItem);

    // Get all food items from database
    ArrayList<MenuItem> fetchAllMenuItems();

    // Find one food item by its ID
    MenuItem findMenuItemById(int menuId);

    // Update an existing food item
    boolean updateMenuItem(MenuItem menuItem);

    // Delete a food item from database
    boolean deleteMenuItem(int menuId);

    // Get only food items from one category (like Nepali or Chinese)
    ArrayList<MenuItem> fetchMenuItemsByCategory(String category);

    // Search for food items by name (like searching for "momo")
    ArrayList<MenuItem> searchMenuItemsByName(String keyword);
}