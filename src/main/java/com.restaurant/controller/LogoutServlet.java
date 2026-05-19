/*
 * LogoutServlet.java
 * Signs the user out by clearing their session and sending them to the login page.
 */

package com.restaurant.controller; // Controller package for servlet endpoints

import jakarta.servlet.ServletException; // Thrown when servlet forwarding fails
import jakarta.servlet.annotation.WebServlet; // Maps this class to a URL path
import jakarta.servlet.http.HttpServlet; // Base class for HTTP request handling
import jakarta.servlet.http.HttpServletRequest; // Incoming request data
import jakarta.servlet.http.HttpServletResponse; // Outgoing response to the browser
import jakarta.servlet.http.HttpSession; // Server-side session tied to the user

import java.io.IOException; // Thrown on redirect or I/O errors

@WebServlet("/logout") // Handles GET requests to /logout
public class LogoutServlet extends HttpServlet { // Servlet that ends the user's session
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { // Runs when user visits logout
        HttpSession session = request.getSession(false); // Reuse existing session only; do not create a new one
        if (session != null) { // Only invalidate if the user was actually logged in
            session.invalidate(); // Clear all session data so they are fully signed out
        }
        response.sendRedirect(request.getContextPath() + "/login"); // Send them back to the login screen
    }
}
