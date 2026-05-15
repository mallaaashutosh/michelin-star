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

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    private MenuDaoImpl menuDao;

    // this runs when servlet first starts
    @Override
    public void init() {
        menuDao = new MenuDaoImpl();
    }

    // handles GET requests (viewing data)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String action = request.getParameter("action");

        // no action = show all menu items
        if (action == null) {
            ArrayList<MenuItem> menuItems = menuDao.fetchAllMenuItems();
            request.setAttribute("menuItems", menuItems);
            request.getRequestDispatcher("/views/customer/menu.jsp").forward(request, response);
        }
        // filter by category (like Japanese or Nepali)
        else if ("category".equals(action)) {
            String category = request.getParameter("category");
            ArrayList<MenuItem> menuItems = menuDao.fetchMenuItemsByCategory(category);
            request.setAttribute("menuItems", menuItems);
            request.setAttribute("selectedCategory", category);
            request.getRequestDispatcher("/views/customer/menu.jsp").forward(request, response);
        }
        // search by name (like searching for "momo")
        else if ("search".equals(action)) {
            String keyword = request.getParameter("keyword");
            ArrayList<MenuItem> menuItems = menuDao.searchMenuItemsByName(keyword);
            request.setAttribute("menuItems", menuItems);
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("/views/customer/menu.jsp").forward(request, response);
        }
        // delete a menu item
        else if ("delete".equals(action)) {
            int menuId = Integer.parseInt(request.getParameter("id"));
            menuDao.deleteMenuItem(menuId);
            response.sendRedirect("menu");
        }
    }

    // handles POST requests (adding or updating data)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String action = request.getParameter("action");

        // add new food item
        if ("add".equals(action)) {
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            double price = Double.parseDouble(request.getParameter("price"));
            String availability = request.getParameter("availability");

            MenuItem newItem = new MenuItem(name, category, price, availability);
            menuDao.insertMenuItem(newItem);
            response.sendRedirect("menu");
        }
        // update existing food item
        else if ("edit".equals(action)) {
            int menuId = Integer.parseInt(request.getParameter("menuId"));
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            double price = Double.parseDouble(request.getParameter("price"));
            String availability = request.getParameter("availability");

            MenuItem item = new MenuItem(menuId, name, category, price, availability);
            menuDao.updateMenuItem(item);
            response.sendRedirect("menu");
        }
    }
}