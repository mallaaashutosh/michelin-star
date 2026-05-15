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

    private static final String MENU_VIEW = "/views/customer/menu.jsp";

    private MenuDaoImpl menuDao;

    @Override
    public void init() {
        menuDao = new MenuDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String action = request.getParameter("action");
        String ctx = request.getContextPath();

        if (action == null) {
            forwardMenu(request, response, menuDao.fetchAllMenuItems(), null, null);
        } else if ("category".equals(action)) {
            String category = request.getParameter("category");
            forwardMenu(request, response, menuDao.fetchMenuItemsByCategory(category), category, null);
        } else if ("search".equals(action)) {
            String keyword = request.getParameter("keyword");
            forwardMenu(request, response, menuDao.searchMenuItemsByName(keyword), null, keyword);
        } else if ("delete".equals(action)) {
            int menuId = Integer.parseInt(request.getParameter("id"));
            menuDao.deleteMenuItem(menuId);
            response.sendRedirect(ctx + "/menu");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

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

        response.sendRedirect(ctx + "/menu");
    }

    private void forwardMenu(HttpServletRequest request, HttpServletResponse response,
                             ArrayList<MenuItem> menuItems, String category, String keyword)
            throws ServletException, IOException {
        request.setAttribute("menuItems", menuItems);
        if (category != null) {
            request.setAttribute("selectedCategory", category);
        }
        if (keyword != null) {
            request.setAttribute("searchKeyword", keyword);
        }
        request.getRequestDispatcher(MENU_VIEW).forward(request, response);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
