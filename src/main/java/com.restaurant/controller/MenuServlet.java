package com.restaurant.controller;

import com.restaurant.dao.MenuDaoImpl;
import com.restaurant.entity.MenuItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

// This servlet handles all requests for /menu
@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    // Create MenuDaoImpl object to talk to database
    private MenuDaoImpl menuDao;

    // This runs when servlet first starts
    @Override
    public void init() {
        menuDao = new MenuDaoImpl();
    }

    // Handles GET requests (when user types URL or clicks link)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the action parameter from URL
        String action = request.getParameter("action");

        // If no action, show all menu items
        if (action == null) {
            // Get all food items from database
            ArrayList<MenuItem> menuItems = menuDao.fetchAllMenuItems();
            // Store in request so JSP can access
            request.setAttribute("menuItems", menuItems);
            // Go to menu.jsp page
            request.getRequestDispatcher("/views/customer/menu.jsp").forward(request, response);
        }
        // If action is "category", filter by category
        else if ("category".equals(action)) {
            // Get category name from URL
            String category = request.getParameter("category");
            // Get only items from this category
            ArrayList<MenuItem> menuItems = menuDao.fetchMenuItemsByCategory(category);
            // Store in request
            request.setAttribute("menuItems", menuItems);
            request.setAttribute("selectedCategory", category);
            // Go to menu.jsp
            request.getRequestDispatcher("/views/customer/menu.jsp").forward(request, response);
        }
        // If action is "search", search by name
        else if ("search".equals(action)) {
            // Get keyword from URL
            String keyword = request.getParameter("keyword");
            // Search menu items by name
            ArrayList<MenuItem> menuItems = menuDao.searchMenuItemsByName(keyword);
            // Store in request
            request.setAttribute("menuItems", menuItems);
            request.setAttribute("searchKeyword", keyword);
            // Go to menu.jsp
            request.getRequestDispatcher("/views/customer/menu.jsp").forward(request, response);
        }
    }

    // Handles POST requests (when user submits a form)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Just redirect back to menu page
        response.sendRedirect("menu");
    }
}