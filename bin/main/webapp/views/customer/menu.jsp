<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%
    // If no menu items, go to menu servlet to load them
    if (request.getAttribute("menuItems") == null) {
        response.sendRedirect(request.getContextPath() + "/menu");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Restaurant Menu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
</head>
<body>

<div class="container">

    <!-- TOP BAR WITH CART ICON -->
    <div class="top-bar">
        <!-- Back button to go home -->
        <a href="${pageContext.request.contextPath}/" class="back-btn">← Back to Home</a>

        <h1>Our Menu</h1>

        <div class="cart-icon">
            <a href="${pageContext.request.contextPath}/cart">
                🛒 Cart <span id="cartCount">0</span>
            </a>
        </div>
    </div>

    <!-- SEARCH FORM -->
    <div class="search-box">
        <form action="${pageContext.request.contextPath}/menu" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" placeholder="Search food..." value="<c:out value='${searchKeyword}' default='' />">
            <button type="submit" class="btn">Search</button>
            <a href="${pageContext.request.contextPath}/menu" class="btn">Reset</a>
        </form>
    </div>

    <!-- CATEGORY FILTER FORM -->
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

    <!-- MENU ITEMS GRID -->
    <div class="menu-grid">
        <c:forEach var="item" items="${menuItems}">
            <div class="menu-card">
                <!-- Food image -->
                <c:choose>
                    <c:when test="${not empty item.image}">
                        <img src="${pageContext.request.contextPath}/uploads/${item.image}" alt="${item.name}" class="food-image">
                    </c:when>
                    <c:otherwise>
                        <div class="food-image food-image-placeholder">&#9733;</div>
                    </c:otherwise>
                </c:choose>

                <!-- Food details -->
                <div class="menu-info">
                    <h3>${item.name}</h3>
                    <p class="category">${item.category}</p>
                    <p class="price">Rs. ${item.price}</p>
                    <p class="status">${item.availability}</p>
                </div>

                <!-- Add to cart button -->
                <div class="menu-action">
                    <c:if test="${item.availability == 'available'}">
                        <button class="add-btn"
                                data-id="${item.menuId}"
                                data-name="${item.name}"
                                data-price="${item.price}"
                                onclick="addToCart(this)">+</button>
                    </c:if>
                    <c:if test="${item.availability != 'available'}">
                        <button class="add-btn disabled" disabled>OUT</button>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>

</div>

<!-- JavaScript for adding to cart without page refresh -->
<script>
    // Variable to store current cart count
    let cartItemCount = 0;

    // Function to add item to cart (no page refresh)
    function addToCart(button) {
        // Get item details from button attributes
        let menuId = button.getAttribute("data-id");
        let name = button.getAttribute("data-name");
        let price = button.getAttribute("data-price");

        // Create form data to send
        let formData = new URLSearchParams();
        formData.append("action", "add");
        formData.append("menuId", menuId);
        formData.append("name", name);
        formData.append("price", price);
        formData.append("quantity", "1");

        // Send to cart servlet using fetch (page does not refresh)
        fetch("${pageContext.request.contextPath}/cart", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: formData
        })
            .then(function(response) {
                if (response.ok) {
                    // Increase cart count
                    cartItemCount = cartItemCount + 1;
                    // Update the number shown on cart icon
                    document.getElementById("cartCount").innerText = cartItemCount;
                }
            })
            .catch(function(error) {
                console.log("Error:", error);
            });
    }

    // Load current cart count when page first loads
    window.onload = function() {
        fetch("${pageContext.request.contextPath}/cart?action=count", {
            method: "GET"
        })
            .then(function(response) {
                return response.json();
            })
            .then(function(data) {
                cartItemCount = data.count;
                document.getElementById("cartCount").innerText = cartItemCount;
            })
            .catch(function(error) {
                console.log("Error getting cart count:", error);
            });
    }
</script>

</body>
</html>