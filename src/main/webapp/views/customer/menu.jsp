<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Restaurant Menu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
</head>
<body>

<div class="container">
    <!-- Top bar with title and cart icon -->
    <div class="top-bar">
        <h1>🍽️ Our Menu</h1>
        <div class="cart-icon">
            <a href="${pageContext.request.contextPath}/cart">
                🛒 <span id="cartCount">0</span>
            </a>
        </div>
    </div>

    <!-- Search form - lets customer search food by name -->
    <div class="search-box">
        <form action="${pageContext.request.contextPath}/menu" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" placeholder="Search food..." value="<c:out value='${searchKeyword}' default='' />">
            <button type="submit" class="btn">Search</button>
            <a href="${pageContext.request.contextPath}/menu" class="btn">Reset</a>
        </form>
    </div>

    <!-- Filter form - lets customer filter by category -->
    <div class="search-box">
        <form action="${pageContext.request.contextPath}/menu" method="get">
            <input type="hidden" name="action" value="category">
            <select name="category">
                <option value="">All Categories</option>
                <option value="Japanese" ${selectedCategory == 'Japanese' ? 'selected' : ''}>Japanese</option>
                <option value="Indian" ${selectedCategory == 'Indian' ? 'selected' : ''}>Indian</option>
                <option value="Nepali" ${selectedCategory == 'Nepali' ? 'selected' : ''}>Nepali</option>
                <option value="Italian" ${selectedCategory == 'Italian' ? 'selected' : ''}>Italian</option>
                <option value="Turkish" ${selectedCategory == 'Turkish' ? 'selected' : ''}>Turkish</option>
            </select>
            <button type="submit" class="btn">Filter</button>
        </form>
    </div>

    <!-- Grid of menu items -->
    <div class="menu-grid">
        <!-- Loop through each menu item from database -->
        <c:forEach var="item" items="${menuItems}">
            <div class="menu-card">
                <!-- Left side - food details -->
                <div class="menu-info">
                    <h3>${item.name}</h3>
                    <p class="category">${item.category}</p>
                    <p class="price">Rs. ${item.price}</p>
                    <p class="status ${item.availability != 'available' ? 'unavailable' : ''}">
                            ${item.availability}
                    </p>
                </div>
                <!-- Right side - add button -->
                <div class="menu-action">
                    <c:if test="${item.availability == 'available'}">
                        <button class="add-btn" onclick="addToCart(${item.menuId}, '${item.name}', ${item.price})">
                            +
                        </button>
                    </c:if>
                    <c:if test="${item.availability != 'available'}">
                        <button class="add-btn disabled" disabled>OUT</button>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Place order button at bottom -->
    <div class="order-btn-container">
        <button class="place-order-btn" onclick="goToCart()">Place Order</button>
    </div>
</div>

<!-- Link to main.js file -->
<script src="${pageContext.request.contextPath}/js/main.js"></script>

</body>
</html>