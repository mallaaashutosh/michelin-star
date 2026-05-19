/*
 * CartServlet.java
 * Session-backed shopping cart: add, update, remove, and clear items; expose item count as JSON for the menu UI.
 */
package com.restaurant.controller; // controller package for HTTP endpoints

import jakarta.servlet.ServletException; // servlet errors during forward
import jakarta.servlet.annotation.WebServlet; // URL mapping annotation
import jakarta.servlet.http.HttpServlet; // base servlet type
import jakarta.servlet.http.HttpServletRequest; // incoming request
import jakarta.servlet.http.HttpServletResponse; // outgoing response
import jakarta.servlet.http.HttpSession;  // FIXED: was jakartaarta, now jakarta — session for cart storage
import java.io.IOException; // I/O on redirect or writer

import com.restaurant.entity.Cart; // cart model stored in session

// This servlet handles all cart actions
@WebServlet("/cart") // all cart traffic hits /cart
public class CartServlet extends HttpServlet { // cart CRUD over HTTP

    // Handle POST requests (when user submits a form or fetch request)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) // mutations: add, update, remove, clear
            throws ServletException, IOException { // container may throw

        // Get which action user wants to do
        String action = request.getParameter("action"); // which cart operation to run

        // Get current session
        HttpSession session = request.getSession(); // need session to hold cart

        // Get cart from session
        Cart cart = (Cart) session.getAttribute("cart"); // existing cart or null

        // If no cart exists, create a new one
        if (cart == null) { // first visit or expired session
            cart = new Cart(); // fresh empty cart
            session.setAttribute("cart", cart); // store for later requests
        }

        // ACTION 1: Add item to cart
        if ("add".equals(action)) { // menu page "Add to cart"
            // Get item details from the form
            int menuId = Integer.parseInt(request.getParameter("menuId")); // which dish
            String name = request.getParameter("name"); // display name
            double price = Double.parseDouble(request.getParameter("price")); // unit price
            int quantity = Integer.parseInt(request.getParameter("quantity")); // how many

            // Add to cart
            cart.addItem(menuId, name, price, quantity); // merge or append line

            // Send success response (for JavaScript fetch)
            response.setStatus(HttpServletResponse.SC_OK); // 200 so fetch knows it worked
            return; // done — no redirect for AJAX add
        }

        // ACTION 2: Update quantity (for + and - buttons)
        if ("update".equals(action)) { // change qty on cart page
            // Get item ID and new quantity
            int menuId = Integer.parseInt(request.getParameter("menuId")); // line to change
            int quantity = Integer.parseInt(request.getParameter("quantity")); // new qty

            // Update quantity in cart
            cart.updateQuantity(menuId, quantity); // apply change in memory

            // Stay on cart page
            response.sendRedirect(request.getContextPath() + "/cart"); // PRG back to cart view
            return; // stop processing
        }

        // ACTION 3: Remove one item from cart
        if ("remove".equals(action)) { // delete single line
            // Get item ID to remove
            int menuId = Integer.parseInt(request.getParameter("menuId")); // which line goes away

            // Remove from cart
            cart.removeItem(menuId); // drop that menu id from cart

            // Stay on cart page
            response.sendRedirect(request.getContextPath() + "/cart"); // refresh cart page
            return; // end handler
        }

        // ACTION 4: Clear entire cart
        if ("clear".equals(action)) { // empty everything
            // Remove all items
            cart.clear(); // wipe all lines

            // Stay on cart page
            response.sendRedirect(request.getContextPath() + "/cart"); // show empty cart
            return; // finished
        }

        // Default: go to cart page
        response.sendRedirect(request.getContextPath() + "/cart"); // unknown action — still show cart
    }

    // Handle GET requests (when user opens cart page or asks for cart count)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) // page view or JSON count
            throws ServletException, IOException { // servlet I/O

        // Get action parameter if any
        String action = request.getParameter("action"); // optional ?action=count

        // ACTION: Get cart count as JSON (for menu page cart icon)
        if ("count".equals(action)) { // badge on menu navbar
            // Get session
            HttpSession session = request.getSession(); // session holds cart

            // Get cart from session
            Cart cart = (Cart) session.getAttribute("cart"); // may be null

            // Default count is 0
            int count = 0; // no cart means zero items

            // If cart exists, get item count
            if (cart != null) { // only count when cart exists
                count = cart.getItemCount(); // total units across lines
            }

            // Set response type to JSON
            response.setContentType("application/json"); // client expects JSON

            // Send JSON response like {"count":5}
            response.getWriter().write("{\"count\":" + count + "}"); // minimal JSON payload
            return; // do not forward to JSP
        }

        // Default: Show the cart page
        request.getRequestDispatcher("/views/customer/cart.jsp").forward(request, response); // render cart JSP
    }
}
