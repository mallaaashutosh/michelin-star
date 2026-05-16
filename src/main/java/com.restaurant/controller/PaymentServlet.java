package com.restaurant.controller;

import com.restaurant.entity.Cart;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/menu");
            return;
        }

        request.getRequestDispatcher("/views/customer/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String paymentMethod = request.getParameter("paymentMethod");
        String totalAmount = request.getParameter("totalAmount");
        String tableNumber = request.getParameter("tableNumber");

        HttpSession session = request.getSession();
        session.setAttribute("paymentAmount", totalAmount);
        session.setAttribute("paymentMethod", paymentMethod);
        session.setAttribute("tableNumber", tableNumber);

        response.sendRedirect(request.getContextPath() + "/order");
    }
}