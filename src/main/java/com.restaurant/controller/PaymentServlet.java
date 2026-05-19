/*
 * PaymentServlet.java
 * Shows the checkout payment page and stores payment choices in session before placing an order.
 */

package com.restaurant.controller; // Controller package for servlet endpoints

import com.restaurant.entity.Cart; // Shopping cart held in the user's session
import jakarta.servlet.ServletException; // Thrown when servlet forwarding fails
import jakarta.servlet.annotation.WebServlet; // Maps this class to /payment
import jakarta.servlet.http.HttpServlet; // Base class for HTTP request handling
import jakarta.servlet.http.HttpServletRequest; // Incoming request and form fields
import jakarta.servlet.http.HttpServletResponse; // Outgoing response and redirects
import jakarta.servlet.http.HttpSession; // Stores cart and payment details between steps
import java.io.IOException; // Thrown on redirect or I/O errors

@WebServlet("/payment") // Checkout payment step in the order flow
public class PaymentServlet extends HttpServlet { // Validates cart then captures payment info

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { // Show payment form

        HttpSession session = request.getSession(); // Current session (creates one if needed)
        Cart cart = (Cart) session.getAttribute("cart"); // Cart built on the menu page

        if (cart == null || cart.getItems().isEmpty()) { // Nothing to pay for
            response.sendRedirect(request.getContextPath() + "/menu"); // Send user back to menu
            return; // Do not show payment page
        }

        request.getRequestDispatcher("/views/customer/payment.jsp").forward(request, response); // Render payment JSP
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { // Save payment details and continue checkout

        String paymentMethod = request.getParameter("paymentMethod"); // e.g. card, cash, etc.
        String totalAmount = request.getParameter("totalAmount"); // Order total from the form
        String tableNumber = request.getParameter("tableNumber"); // Dine-in table if provided

        HttpSession session = request.getSession(); // Same session as cart
        session.setAttribute("paymentAmount", totalAmount); // Used on the order confirmation step
        session.setAttribute("paymentMethod", paymentMethod); // How the customer will pay
        session.setAttribute("tableNumber", tableNumber); // Table for kitchen or staff

        response.sendRedirect(request.getContextPath() + "/order"); // Next step: place or confirm order
    }
}
