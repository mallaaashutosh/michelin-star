package com.restaurant.entity;

// This class represents one row in the menu table
// Like a blueprint for a single food item
public class MenuItem {

    // These are the columns from my menu table in database
    private int menuId;        // Primary key - unique ID for each food item
    private String name;       // Name of the food (like "Steam Momo")
    private String category;   // Category (Japanese, Nepali, Italian etc)
    private double price;      // Price of the food item
    private String availability; // Is it available? ("available" or maybe "unavailable")

    // CONSTRUCTOR 1: Used when adding a NEW food item to the database
    // I don't know the menuId yet because database will generate it automatically
    public MenuItem(String name, String category, double price, String availability) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.availability = availability;
    }

    // CONSTRUCTOR 2: Used when getting data FROM the database
    // Here I DO have the menuId because it already exists in database
    public MenuItem(int menuId, String name, String category, double price, String availability) {
        this.menuId = menuId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.availability = availability;
    }

    // GETTER methods - to read the data
    // JSP pages need these to display the data
    public int getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getAvailability() {
        return availability;
    }

    // SETTER methods - to update the data
    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}