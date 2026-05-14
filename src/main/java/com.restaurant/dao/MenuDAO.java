package com.restaurant.dao;

import com.restaurant.entity.MenuItem;
import java.util.ArrayList;

public interface MenuDAO {

    // add new menu item to database
    boolean insertMenuItem(MenuItem menuItem);

    // get all menu items
    ArrayList<MenuItem> fetchAllMenuItems();

    // find one menu item by its id
    MenuItem findMenuItemById(int menuId);

    // update menu item
    boolean updateMenuItem(MenuItem menuItem);

    // delete menu item
    boolean deleteMenuItem(int menuId);

    // get menu items by category (like Japanese, Nepali)
    ArrayList<MenuItem> fetchMenuItemsByCategory(String category);

    // search menu items by name
    ArrayList<MenuItem> searchMenuItemsByName(String keyword);
}