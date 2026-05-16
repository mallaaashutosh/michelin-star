<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%
    List<Map<String, Object>> orders = (List<Map<String, Object>>) request.getAttribute("orders");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Orders</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderlist.css">
</head>
<body>
<div class="container">
    <a href="${pageContext.request.contextPath}/menu" class="back-btn">← Back to Menu</a>
    <h1>My Orders</h1>

    <% if (orders == null || orders.isEmpty()) { %>
    <div class="empty-message">
        <p>You haven't placed any orders yet.</p>
        <a href="${pageContext.request.contextPath}/menu" class="btn">Start Shopping</a>
    </div>
    <% } else { %>
    <div class="table-responsive">
        <table>
            <thead>
            <tr>
                <th>Order ID</th>
                <th>Item Name</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Total</th>
                <th>Table No</th>
                <th>Payment</th>
                <th>Status</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
            <% for (Map<String, Object> order : orders) { %>
            <tr>
                <td><%= order.get("order_id") %></td>
                <td><%= order.get("menu_name") %></td>
                <td><%= order.get("quantity") %></td>
                <td>Rs. <%= order.get("price") %></td>
                <td>Rs. <%= order.get("total_amount") %></td>
                <td><%= order.get("table_number") %></td>
                <td><%= order.get("payment_method") %></td>
                <td><%= order.get("status") %></td>
                <td><%= order.get("created_at") %></td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
    <% } %>
</div>
</body>
</html>