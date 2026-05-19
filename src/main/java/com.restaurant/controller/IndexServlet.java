/*
 * IndexServlet.java
 * Serves the public home page for logged-in customers at / and /site.
 */

package com.restaurant.controller; // Controller package for servlet endpoints

import com.restaurant.entity.User; // Logged-in customer model stored in session
import jakarta.servlet.ServletException; // Thrown when servlet forwarding fails
import jakarta.servlet.annotation.WebServlet; // Maps this class to URL paths
import jakarta.servlet.http.HttpServlet; // Base class for HTTP request handling
import jakarta.servlet.http.HttpServletRequest; // Incoming request data
import jakarta.servlet.http.HttpServletResponse; // Outgoing response to the browser
import jakarta.servlet.http.HttpSession; // Server-side session holding the current user
import java.io.IOException; // Thrown on redirect or I/O errors

@WebServlet(urlPatterns = {"", "/site"}) // Home page at root and at /site
public class IndexServlet extends HttpServlet { // Entry point for the customer landing page

    private static final String HOME_VIEW = "/WEB-INF/views/home.jsp"; // JSP shown after login check passes

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { // Runs when someone opens the home URL
        HttpSession session = request.getSession(false); // Look for an existing session without creating one
        User user = session != null ? (User) session.getAttribute("user") : null; // Pull user from session if present
        String ctx = request.getContextPath(); // App context prefix for redirects (e.g. /michelin-star)

        if (user == null) { // Guests must sign in before seeing the home page
            response.sendRedirect(ctx + "/login"); // Send unauthenticated visitors to login
            return; // Stop here; no view to render
        }

        request.getRequestDispatcher(HOME_VIEW).forward(request, response); // Show the home JSP to logged-in users
    }
}
