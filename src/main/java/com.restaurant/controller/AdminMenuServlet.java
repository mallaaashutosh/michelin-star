package com.restaurant.controller;

import com.restaurant.dao.MenuDaoImpl;
import com.restaurant.entity.MenuItem;
import com.restaurant.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/admin/menu")
public class AdminMenuServlet extends HttpServlet {

    private MenuDaoImpl menuDao;

    @Override
    public void init() {
        menuDao = new MenuDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int menuId = Integer.parseInt(request.getParameter("id"));
            menuDao.deleteMenuItem(menuId);
            response.sendRedirect(request.getContextPath() + "/admin/menu");
            return;
        }

        if ("edit".equals(action)) {
            int menuId = Integer.parseInt(request.getParameter("id"));
            MenuItem item = menuDao.findMenuItemById(menuId);
            request.setAttribute("editItem", item);
        }

        ArrayList<MenuItem> menuItems = menuDao.fetchAllMenuItems();
        request.setAttribute("menuItems", menuItems);
        request.getRequestDispatcher("/views/admin/menu-manage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        String ctx = request.getContextPath();

        if ("add".equals(action)) {
            MenuItem newItem = new MenuItem(
                    request.getParameter("name"),
                    request.getParameter("category"),
                    Double.parseDouble(request.getParameter("price")),
                    nullToEmpty(request.getParameter("image")),
                    request.getParameter("availability"));
            menuDao.insertMenuItem(newItem);
        } else if ("edit".equals(action)) {
            int menuId = Integer.parseInt(request.getParameter("menuId"));
            MenuItem existing = menuDao.findMenuItemById(menuId);
            String image = request.getParameter("image");
            if (image == null || image.isBlank()) {
                image = existing != null ? existing.getImage() : "";
            }
            MenuItem item = new MenuItem(
                    menuId,
                    request.getParameter("name"),
                    request.getParameter("category"),
                    Double.parseDouble(request.getParameter("price")),
                    image,
                    request.getParameter("availability"));
            menuDao.updateMenuItem(item);
        }

        response.sendRedirect(ctx + "/admin/menu");
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        User user = (User) session.getAttribute("user");
        return user != null && user.isAdmin();
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
