-- Create database if not exists
CREATE DATABASE IF NOT EXISTS restaurant;
USE restaurant;

-- Drop tables in correct order (child tables first)
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart;     -- REMOVED: We are using SESSION for cart
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS user;

-- TABLE: user
CREATE TABLE user (
                      user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      phone_number VARCHAR(20),
                      password VARCHAR(255) NOT NULL,
                      role ENUM('admin', 'customer') NOT NULL,
                      status VARCHAR(20) DEFAULT 'pending',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- TABLE: menu
CREATE TABLE menu (
                      menu_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      category VARCHAR(255) NOT NULL,
                      price DECIMAL(10,2) NOT NULL,
                      image VARCHAR(500),
                      availability VARCHAR(20) DEFAULT 'available',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- TABLE: orders (saved after payment)
CREATE TABLE orders (
                        order_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        customer_id INT NOT NULL,
                        menu_id INT NOT NULL,
                        menu_name VARCHAR(255) NOT NULL,
                        quantity INT NOT NULL DEFAULT 1,
                        price DECIMAL(10,2) NOT NULL,
                        total_amount DECIMAL(10,2) NOT NULL,
                        table_number INT,
                        payment_method VARCHAR(50) DEFAULT 'cash',
                        status VARCHAR(20) DEFAULT 'pending',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (customer_id) REFERENCES user(user_id),
                        FOREIGN KEY (menu_id) REFERENCES menu(menu_id)
);

-- TABLE: payment (saved after payment)
CREATE TABLE payment (
                         payment_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         customer_id INT NOT NULL,
                         total_amount DECIMAL(10,2) NOT NULL,
                         method VARCHAR(50) DEFAULT 'cash',
                         status VARCHAR(20) DEFAULT 'paid',
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (customer_id) REFERENCES user(user_id)
);