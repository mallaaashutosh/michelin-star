package com.restaurant.controller;

import com.restaurant.dao.OrderDAO;
import com.restaurant.dao.OrderDaoImpl;
import com.restaurant.entity.Cart;
import com.restaurant.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

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

        // Get payment details from session
        String totalAmount = (String) session.getAttribute("paymentAmount");
        String paymentMethod = (String) session.getAttribute("paymentMethod");
        String tableNumberStr = (String) session.getAttribute("tableNumber");

        int tableNumber = 0;
        if (tableNumberStr != null) {
            tableNumber = Integer.parseInt(tableNumberStr);
        }

        // Get cart
        Cart cart = (Cart) session.getAttribute("cart");

        // Save order
        boolean saved = orderDAO.saveOrder(cart, customerId, paymentMethod, tableNumber);

        if (saved) {
            // Clear cart
            if (cart != null) {
                cart.clear();
                session.setAttribute("cart", cart);
            }

            // Clean session
            session.removeAttribute("paymentAmount");
            session.removeAttribute("paymentMethod");
            session.removeAttribute("tableNumber");

            // Show success page
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("paymentMethod", paymentMethod);
            request.setAttribute("tableNumber", tableNumber);
            request.getRequestDispatcher("/views/customer/orderSuccess.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}