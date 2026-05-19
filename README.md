# 🍽️ Michelin Star - Restaurant Management System

A full-featured **restaurant management system** built with Java Servlets, JSP, and MySQL. It allows customers to browse the menu, add items to cart, place orders, and make payments, while providing admins with tools to manage users, menus, and orders.

![Java](https://img.shields.io/badge/Java-83.1%25-blue)
![CSS](https://img.shields.io/badge/CSS-16.9%25-orange)
![License](https://img.shields.io/badge/license-MIT-green)

## ✨ Features

### 👤 User Features
- **User Registration & Login** with strong validation
- Browse restaurant menu with images
- Add/remove items to cart
- Place orders and view order history
- Secure payment simulation

### 🔧 Admin Features
- Manage menu items (add/edit/delete)
- User management
- View and manage orders
- Dashboard with overview

### 🛡️ Security & Validation
- Client-side + Server-side email validation
- Strong password enforcement (8+ chars, uppercase, lowercase, number, special character)
- Session-based authentication
- Input sanitization and error handling

## 🛠️ Tech Stack

- **Backend**: Java (Servlets 4.0+)
- **Frontend**: JSP, HTML5, CSS3, JavaScript
- **Database**: MySQL
- **Build Tool**: Maven
- **Server**: Apache Tomcat (recommended)

## 🚀 Quick Start

### 1. Prerequisites
- Java 8 or higher
- Apache Maven
- MySQL Server
- Apache Tomcat 9/10

### 2. Database Setup
```bash
# Create database
mysql -u root -p
CREATE DATABASE michelin_star;

# Import schema and seed data
mysql -u root -p michelin_star < sql/Restaurant.sql
mysql -u root -p michelin_star < sql/seed.sql
