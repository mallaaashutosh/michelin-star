<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    String totalAmount = (String) request.getAttribute("totalAmount");
    String paymentMethod = (String) request.getAttribute("paymentMethod");
    Integer tableNumber = (Integer) request.getAttribute("tableNumber");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Successful</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/success.css">
</head>
<body>
<div class="success-container">
    <div class="checkmark">✓</div>
    <h1>Order Successful!</h1>
    <p>Thank you for your order.</p>

    <div class="order-details">
        <p><strong>Table Number:</strong> <%= tableNumber %></p>
        <p><strong>Payment Method:</strong> <%= paymentMethod %></p>
        <p><strong>Total Amount:</strong> Rs. <%= totalAmount %></p>
    </div>

    <p>Your food will be served shortly.</p>

    <div class="buttons">
        <a href="${pageContext.request.contextPath}/menu" class="btn">Continue Shopping</a>
        <a href="${pageContext.request.contextPath}/orderlist" class="btn btn-order">View My Orders</a>
    </div>
</div>
</body>
</html>