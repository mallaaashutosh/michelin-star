<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Restaurant Menu</title>
    <!-- Link to CSS file for styling -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
</head>
<body>

<div class="container">

    <!-- ========== TOP BAR WITH CART ICON ========== -->
    <div class="top-bar">
        <h1>Our Menu</h1>
        <div class="cart-icon">
            <a href="${pageContext.request.contextPath}/cart">
                🛒 Cart <span id="cartCount">0</span>
            </a>
        </div>
    </div>

    <!-- ========== SEARCH FORM ========== -->
    <!-- Lets customer search food by name -->
    <div class="search-box">
        <form action="${pageContext.request.contextPath}/menu" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" placeholder="Search food..." value="<c:out value='${searchKeyword}' default='' />">
            <button type="submit" class="btn">Search</button>
            <a href="${pageContext.request.contextPath}/menu" class="btn">Reset</a>
        </form>
    </div>

    <!-- ========== CATEGORY FILTER FORM ========== -->
    <!-- Lets customer filter food by category (Nepali, Chinese, etc) -->
    <div class="search-box">
        <form action="${pageContext.request.contextPath}/menu" method="get">
            <input type="hidden" name="action" value="category">
            <select name="category">
                <option value="">All Categories</option>
                <option value="Nepali" ${selectedCategory == 'Nepali' ? 'selected' : ''}>Nepali</option>
                <option value="Chinese" ${selectedCategory == 'Chinese' ? 'selected' : ''}>Chinese</option>
                <option value="Indian" ${selectedCategory == 'Indian' ? 'selected' : ''}>Indian</option>
                <option value="Italian" ${selectedCategory == 'Italian' ? 'selected' : ''}>Italian</option>
                <option value="FastFood" ${selectedCategory == 'FastFood' ? 'selected' : ''}>Fast Food</option>
                <option value="Beverages" ${selectedCategory == 'Beverages' ? 'selected' : ''}>Beverages</option>
            </select>
            <button type="submit" class="btn">Filter</button>
        </form>
    </div>

    <!-- ========== MENU ITEMS GRID ========== -->
    <!-- Loop through all menu items from database and display each one -->
    <div class="menu-grid">
        <c:forEach var="item" items="${menuItems}">
            <div class="menu-card">
                <!-- Food image - loaded from ImageServlet -->
                <img src="${pageContext.request.contextPath}/uploads/${item.image}" alt="${item.name}" class="food-image">

                <!-- Food details (name, category, price, status) -->
                <div class="menu-info">
                    <h3>${item.name}</h3>
                    <p class="category">${item.category}</p>
                    <p class="price">Rs. ${item.price}</p>
                    <p class="status">${item.availability}</p>
                </div>

                <!-- Add to cart button -->
                <div class="menu-action">
                    <button class="add-btn" onclick="addToCart(${item.menuId}, '${item.name}', ${item.price})">
                        +
                    </button>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- ========== PLACE ORDER BUTTON ========== -->
    <!-- Takes customer to cart page to review order -->
    <div class="order-btn-container">
        <button class="place-order-btn" onclick="goToCart()">Place Order</button>
    </div>
</div>

<!-- Link to JavaScript file for cart functions -->
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>