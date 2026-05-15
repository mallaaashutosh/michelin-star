package com.restaurant.entity;

// This class holds data for one food item from the menu table
public class MenuItem {

    // These match the columns in my menu table
    private int menuId;
    private String name;
    private String category;
    private double price;
    private String image;
    private String availability;

    // Used when adding a new food item to database
    // menuId will be created by database automatically
    public MenuItem(String name, String category, double price, String image, String availability) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
        this.availability = availability;
    }

    // Used when getting food items from database
    // menuId comes from the database
    public MenuItem(int menuId, String name, String category, double price, String image, String availability) {
        this.menuId = menuId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
        this.availability = availability;
    }

    // Getter methods
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

    public String getImage() {
        return image;
    }

    public String getAvailability() {
        return availability;
    }

    // Setter methods
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

    public void setImage(String image) {
        this.image = image;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}