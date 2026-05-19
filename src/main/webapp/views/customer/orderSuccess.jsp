<%--
  orderSuccess.jsp — Thank-you page after checkout. Shows table number, payment method, and
  total from OrderServlet; offers links back to the menu or to full order history.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%-- UTF-8 page --%>

<%
    String totalAmount = (String) request.getAttribute("totalAmount"); // Bill total as string
    String paymentMethod = (String) request.getAttribute("paymentMethod"); // Cash, Card, Online
    Integer tableNumber = (Integer) request.getAttribute("tableNumber"); // Dine-in table picked at payment
%>

<!DOCTYPE html> <!-- HTML5 -->
<html> <!-- Root -->
<head> <!-- Head -->
    <meta charset="UTF-8"> <!-- Encoding -->
    <title>Order Successful</title> <!-- Tab title -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/success.css"> <!-- Success page styles -->
</head>
<body> <!-- Body -->
<div class="success-container"> <!-- Centered confirmation card -->
    <div class="checkmark">✓</div> <!-- Green check icon -->
    <h1>Order Successful!</h1> <!-- Main headline -->
    <p>Thank you for your order.</p> <!-- Short thank-you -->

    <div class="order-details"> <!-- Receipt-style summary -->
        <p><strong>Table Number:</strong> <%= tableNumber %></p> <!-- Where food is served -->
        <p><strong>Payment Method:</strong> <%= paymentMethod %></p> <!-- How they paid -->
        <p><strong>Total Amount:</strong> Rs. <%= totalAmount %></p> <!-- Amount charged -->
    </div>

    <p>Your food will be served shortly.</p> <!-- Set expectation -->

    <div class="buttons"> <!-- Next steps -->
        <a href="${pageContext.request.contextPath}/menu" class="btn">Continue Shopping</a> <!-- Order more -->
        <a href="${pageContext.request.contextPath}/orderlist" class="btn btn-order">View My Orders</a> <!-- See history -->
    </div>
</div>
</body>
</html>
