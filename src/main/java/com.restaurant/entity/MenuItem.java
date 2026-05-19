/*
 * Model for a single dish on the restaurant menu.
 * Fields line up with the menu table columns we read and write in MenuDAO.
 * Two constructors cover insert (no id yet) and select (id from the database).
 */
package com.restaurant.entity; // entity package for menu models

// This class holds data for one food item from the menu table
public class MenuItem { // one row from the menu table

    // These match the columns in my menu table
    private int menuId; // primary key
    private String name; // dish name shown to customers
    private String category; // e.g. Nepali, Italian, Drinks
    private double price; // price in rupees (or project currency)
    private String image; // filename or path under webapp/image
    private String availability; // in stock / sold out flag

    // Used when adding a new food item to database
    // menuId will be created by database automatically
    public MenuItem(String name, String category, double price, String image, String availability) {
        this.name = name; // set dish name
        this.category = category; // set category
        this.price = price; // set price
        this.image = image; // set image path
        this.availability = availability; // set stock flag
    }

    // Used when getting food items from database
    // menuId comes from the database
    public MenuItem(int menuId, String name, String category, double price, String image, String availability) {
        this.menuId = menuId; // set id from DB
        this.name = name; // set dish name
        this.category = category; // set category
        this.price = price; // set price
        this.image = image; // set image path
        this.availability = availability; // set stock flag
    }

    // Getter methods
    public int getMenuId() {
        return menuId; // return primary key
    }

    public String getName() {
        return name; // return dish name
    }

    public String getCategory() {
        return category; // return category label
    }

    public double getPrice() {
        return price; // return unit price
    }

    public String getImage() {
        return image; // return image filename/path
    }

    public String getAvailability() {
        return availability; // return in-stock / sold-out text
    }

    // Setter methods
    public void setMenuId(int menuId) {
        this.menuId = menuId; // update primary key (rare)
    }

    public void setName(String name) {
        this.name = name; // update dish name
    }

    public void setCategory(String category) {
        this.category = category; // update category
    }

    public void setPrice(double price) {
        this.price = price; // update price
    }

    public void setImage(String image) {
        this.image = image; // update image path
    }

    public void setAvailability(String availability) {
        this.availability = availability; // update availability flag
    }
}
