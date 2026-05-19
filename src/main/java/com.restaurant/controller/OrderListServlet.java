/*
 * OrderListServlet.java
 * Loads a customer's past orders and displays them on the order history page.
 */

package com.restaurant.controller; // Controller package for servlet endpoints

import com.restaurant.dao.OrderDAO; // Contract for fetching orders
import com.restaurant.dao.OrderDaoImpl; // JDBC implementation of order queries
import com.restaurant.entity.User; // Logged-in customer from session
import jakarta.servlet.ServletException; // Thrown when servlet forwarding fails
import jakarta.servlet.annotation.WebServlet; // Maps this class to /orderlist
import jakarta.servlet.http.HttpServlet; // Base class for HTTP request handling
import jakarta.servlet.http.HttpServletRequest; // Incoming request
import jakarta.servlet.http.HttpServletResponse; // Outgoing response to JSP
import jakarta.servlet.http.HttpSession; // Holds the current user
import java.io.IOException; // Thrown on forward or I/O errors
import java.util.List; // List of order rows for the JSP
import java.util.Map; // Each order row as column name -> value

@WebServlet("/orderlist") // Customer order history page
public class OrderListServlet extends HttpServlet { // Lists orders for the logged-in user

    private OrderDAO orderDAO; // Data access for order queries

    @Override
    public void init() { // Called once when servlet is loaded
        orderDAO = new OrderDaoImpl(); // Wire up JDBC order DAO
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { // Load orders and show list JSP

        HttpSession session = request.getSession(); // Session with logged-in user

        // Get logged in user
        User user = (User) session.getAttribute("user");
        int customerId = (user != null) ? user.getId() : 1; // Fallback id when session has no user

        // Get orders
        List<Map<String, Object>> orders = orderDAO.getOrdersByCustomerId(customerId);

        // Send to JSP
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/views/customer/orderlist.jsp").forward(request, response); // Render order list
    }
}
