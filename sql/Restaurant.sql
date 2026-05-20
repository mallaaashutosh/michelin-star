-- Schema setup for the Michelin Star restaurant web app.
-- Creates the restaurant database from scratch, drops old tables if they exist,
-- and defines tables for users, menu items, cart, orders, and payments.
-- Run this file first on a fresh MySQL install before loading seed data.

-- Create the database if it does not already exist
CREATE DATABASE IF NOT EXISTS restaurant;
-- Switch to the restaurant database for all statements below
USE restaurant;


-- Remove dependent tables first (child tables before parents)
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS user;

-- Accounts for admins and customers who log in to the app
CREATE TABLE user (
                      user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- auto-generated account id
                      name VARCHAR(255) NOT NULL, -- display name
                      email VARCHAR(255) NOT NULL UNIQUE, -- login email, must be unique
                      phone_number VARCHAR(20), -- optional contact number
                      password VARCHAR(255) NOT NULL, -- stored password (hash in production)
                      role ENUM('admin', 'customer') NOT NULL, -- who can access admin vs customer areas
                      status VARCHAR(20) DEFAULT 'pending', -- active, pending, etc.
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- when the account was created
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- last profile change
);


-- Dishes and drinks shown on the customer menu
CREATE TABLE menu (
                      menu_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- auto-generated dish id
                      name VARCHAR(255) NOT NULL, -- item name shown on the menu
                      category VARCHAR(255) NOT NULL, -- e.g. Nepali, Chinese, Beverages
                      price DECIMAL(10,2) NOT NULL, -- price in rupees
                      image VARCHAR(500), -- filename under webapp/image
                      availability VARCHAR(20) DEFAULT 'available', -- available or sold out
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- when the item was added
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- last menu edit
);


-- CART TABLE (cleared after payment)
-- Items a customer has added before checkout; one row per menu item in the cart
CREATE TABLE cart (
                      cart_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- auto-generated cart line id
                      customer_id INT NOT NULL, -- who owns this cart row
                      menu_id INT NOT NULL, -- which dish was added
                      quantity INT NOT NULL DEFAULT 1, -- how many of that dish
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- when the item was added to cart
                      FOREIGN KEY (customer_id) REFERENCES user(user_id), -- must be a real user
                      FOREIGN KEY (menu_id) REFERENCES menu(menu_id) -- must be a real menu item
);

-- Placed orders with line-item detail, table number, and fulfillment status
CREATE TABLE orders (
                        order_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- auto-generated order line id
                        customer_id INT NOT NULL, -- who placed the order
                        menu_id INT NOT NULL, -- dish ordered (for joins)
                        menu_name VARCHAR(255) NOT NULL,
                        quantity INT NOT NULL DEFAULT 1, -- units ordered
                        price DECIMAL(10,2) NOT NULL, -- unit price at order time
                        total_amount DECIMAL(10,2) NOT NULL, -- quantity × price for this line
                        table_number INT NOT NULL, -- dine-in table number
                        payment_method VARCHAR(50) DEFAULT 'cash', -- cash, card, etc.
                        status VARCHAR(20) DEFAULT 'pending', -- pending, completed, etc.
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- when the order was placed
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- last status change
                        FOREIGN KEY (customer_id) REFERENCES user(user_id), -- must be a real user
                        FOREIGN KEY (menu_id) REFERENCES menu(menu_id) -- must be a real menu item
);

-- Payment records tied to a customer after an order is settled
CREATE TABLE payment (
                         payment_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- auto-generated payment id
                         customer_id INT NOT NULL, -- who paid
                         total_amount DECIMAL(10,2) NOT NULL, -- amount charged for the bill
                         method VARCHAR(50) DEFAULT 'cash', -- how they paid
                         status VARCHAR(20) DEFAULT 'paid', -- payment status
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- when payment was recorded
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- last payment update
                         FOREIGN KEY (customer_id) REFERENCES user(user_id) -- must be a real user
);
