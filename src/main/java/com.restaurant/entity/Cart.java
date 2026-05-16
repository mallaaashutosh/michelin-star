package com.restaurant.entity;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    // List to store items
    private List<CartItem> items = new ArrayList<>();

    // Inner class for one item
    public static class CartItem {
        private int menuId;
        private String name;
        private double price;
        private int quantity;

        // Constructor
        public CartItem(int menuId, String name, double price, int quantity) {
            this.menuId = menuId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        // Getters
        public int getMenuId() { return menuId; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }

        // Setter
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // Add item to cart
    public void addItem(int menuId, String name, double price, int quantity) {
        // Check if item already in cart
        for (CartItem item : items) {
            if (item.getMenuId() == menuId) {
                // Increase quantity if found
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // Add new item if not found
        items.add(new CartItem(menuId, name, price, quantity));
    }


    // Update quantity of an item
    public void updateQuantity(int menuId, int newQuantity) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getMenuId() == menuId) {
                if (newQuantity <= 0) {
                    // Remove if quantity is 0 or less
                    items.remove(i);
                } else {
                    // Update to new quantity
                    item.setQuantity(newQuantity);
                }
                return;
            }
        }
    }
    // ========== END OF NEW METHOD ==========

    // Get all items
    public List<CartItem> getItems() {
        return items;
    }

    // Calculate total price
    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total = total + (item.getPrice() * item.getQuantity());
        }
        return total;
    }

    // Get total number of items
    public int getItemCount() {
        int count = 0;
        for (CartItem item : items) {
            count = count + item.getQuantity();
        }
        return count;
    }

    // Remove one item
    public void removeItem(int menuId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getMenuId() == menuId) {
                items.remove(i);
                break;
            }
        }
    }

    // Clear all items
    public void clear() {
        items.clear();
    }
}