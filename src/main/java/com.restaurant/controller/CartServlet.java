package com.restaurant.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;  // FIXED: was jakartaarta, now jakarta
import java.io.IOException;

import com.restaurant.entity.Cart;

// This servlet handles all cart actions
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    // Handle POST requests (when user submits a form or fetch request)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get which action user wants to do
        String action = request.getParameter("action");

        // Get current session
        HttpSession session = request.getSession();

        // Get cart from session
        Cart cart = (Cart) session.getAttribute("cart");

        // If no cart exists, create a new one
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // ACTION 1: Add item to cart
        if ("add".equals(action)) {
            // Get item details from the form
            int menuId = Integer.parseInt(request.getParameter("menuId"));
            String name = request.getParameter("name");
            double price = Double.parseDouble(request.getParameter("price"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Add to cart
            cart.addItem(menuId, name, price, quantity);

            // Send success response (for JavaScript fetch)
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // ACTION 2: Update quantity (for + and - buttons)
        if ("update".equals(action)) {
            // Get item ID and new quantity
            int menuId = Integer.parseInt(request.getParameter("menuId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Update quantity in cart
            cart.updateQuantity(menuId, quantity);

            // Stay on cart page
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // ACTION 3: Remove one item from cart
        if ("remove".equals(action)) {
            // Get item ID to remove
            int menuId = Integer.parseInt(request.getParameter("menuId"));

            // Remove from cart
            cart.removeItem(menuId);

            // Stay on cart page
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // ACTION 4: Clear entire cart
        if ("clear".equals(action)) {
            // Remove all items
            cart.clear();

            // Stay on cart page
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        // Default: go to cart page
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    // Handle GET requests (when user opens cart page or asks for cart count)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get action parameter if any
        String action = request.getParameter("action");

        // ACTION: Get cart count as JSON (for menu page cart icon)
        if ("count".equals(action)) {
            // Get session
            HttpSession session = request.getSession();

            // Get cart from session
            Cart cart = (Cart) session.getAttribute("cart");

            // Default count is 0
            int count = 0;

            // If cart exists, get item count
            if (cart != null) {
                count = cart.getItemCount();
            }

            // Set response type to JSON
            response.setContentType("application/json");

            // Send JSON response like {"count":5}
            response.getWriter().write("{\"count\":" + count + "}");
            return;
        }

        // Default: Show the cart page
        request.getRequestDispatcher("/views/customer/cart.jsp").forward(request, response);
    }
}