package com.restaurant.controller;

import com.restaurant.dao.OrderDAO;
import com.restaurant.dao.OrderDaoImpl;
import com.restaurant.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/orderlist")
public class OrderListServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() {
        orderDAO = new OrderDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Get logged in user
        User user = (User) session.getAttribute("user");
        int customerId = (user != null) ? user.getId() : 1;

        // Get orders
        List<Map<String, Object>> orders = orderDAO.getOrdersByCustomerId(customerId);

        // Send to JSP
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/views/customer/orderlist.jsp").forward(request, response);
    }
}