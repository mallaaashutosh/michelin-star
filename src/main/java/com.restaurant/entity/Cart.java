/*
 * In-memory shopping cart kept in the user's HTTP session while they browse the menu.
 * Tracks menu items, quantities, and prices; merges duplicate dishes instead of adding twice.
 * Provides helpers to update quantities, compute totals, and clear the cart after checkout.
 */
package com.restaurant.entity; // entity package for session-friendly models

import java.util.ArrayList; // backing list for cart line items
import java.util.List; // return type for getItems()

public class Cart { // session-scoped cart for one customer

    // List to store items
    private List<CartItem> items = new ArrayList<>(); // grows as customer adds dishes

    // Inner class for one item
    public static class CartItem { // one row in the cart (one menu item + qty)
        private int menuId; // FK to menu table
        private String name; // dish name shown on cart page
        private double price; // unit price at time of add
        private int quantity; // how many of this dish

        // Constructor
        public CartItem(int menuId, String name, double price, int quantity) {
            this.menuId = menuId; // remember which menu row
            this.name = name; // copy name for display
            this.price = price; // copy price at add time
            this.quantity = quantity; // starting quantity
        }

        // Getters
        public int getMenuId() { return menuId; } // which dish
        public String getName() { return name; } // dish label
        public double getPrice() { return price; } // unit price
        public int getQuantity() { return quantity; } // how many ordered

        // Setter
        public void setQuantity(int quantity) { this.quantity = quantity; } // change qty from cart UI
    }

    // Add item to cart
    public void addItem(int menuId, String name, double price, int quantity) {
        // Check if item already in cart
        for (CartItem item : items) { // look for same menu id
            if (item.getMenuId() == menuId) {
                // Increase quantity if found
                item.setQuantity(item.getQuantity() + quantity);
                return; // done — no new line needed
            }
        }
        // Add new item if not found
        items.add(new CartItem(menuId, name, price, quantity));
    }


    // Update quantity of an item
    public void updateQuantity(int menuId, int newQuantity) {
        for (int i = 0; i < items.size(); i++) { // index loop so we can remove while iterating
            CartItem item = items.get(i); // current line
            if (item.getMenuId() == menuId) { // match the dish we are editing
                if (newQuantity <= 0) {
                    // Remove if quantity is 0 or less
                    items.remove(i);
                } else {
                    // Update to new quantity
                    item.setQuantity(newQuantity);
                }
                return; // found the item, stop searching
            }
        }
    }
    // ========== END OF NEW METHOD ==========

    // Get all items
    public List<CartItem> getItems() {
        return items; // JSP iterates this list to render the cart
    }

    // Calculate total price
    public double getTotal() {
        double total = 0; // running sum
        for (CartItem item : items) { // each line in the cart
            total = total + (item.getPrice() * item.getQuantity()); // line total = price × qty
        }
        return total; // amount due before tax/shipping if any
    }

    // Get total number of items
    public int getItemCount() {
        int count = 0; // sum of all quantities (not just distinct dishes)
        for (CartItem item : items) { // every cart line
            count = count + item.getQuantity(); // add this line's qty
        }
        return count; // badge number on cart icon
    }

    // Remove one item
    public void removeItem(int menuId) {
        for (int i = 0; i < items.size(); i++) { // scan by index
            if (items.get(i).getMenuId() == menuId) { // found the dish to drop
                items.remove(i);
                break; // only one line per menu id
            }
        }
    }

    // Clear all items
    public void clear() {
        items.clear(); // empty cart after successful order
    }
}
