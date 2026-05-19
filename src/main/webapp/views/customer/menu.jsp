<%--
  menu.jsp — Customer-facing menu page. Shows dishes from MenuServlet with search and
  category filters; items can be added to the cart via fetch without reloading the page.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%-- Tell the server this page is UTF-8 HTML --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %> <%-- JSTL core tags for loops and output --%>

<%
    // If no menu items, go to menu servlet to load them
    if (request.getAttribute("menuItems") == null) { // Nothing loaded yet — ask the servlet
        response.sendRedirect(request.getContextPath() + "/menu"); // Reload via /menu
        return; // Stop rendering this page
    }
%>

<!DOCTYPE html> <!-- HTML5 document -->
<html> <!-- Root element -->
<head> <!-- Page metadata and styles -->
    <meta charset="UTF-8"> <!-- Character encoding -->
    <title>Restaurant Menu</title> <!-- Browser tab title -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css"> <!-- Menu page styles -->
</head>
<body> <!-- Visible page content -->

<div class="container"> <!-- Centered layout wrapper -->

    <!-- TOP BAR WITH CART ICON -->
    <div class="top-bar"> <!-- Header row: back link, title, cart -->
        <!-- Back button to go home -->
        <a href="${pageContext.request.contextPath}/" class="back-btn">← Back to Home</a> <!-- Return to landing -->

        <h1>Our Menu</h1> <!-- Page heading -->

        <div class="cart-icon"> <!-- Cart link with live count -->
            <a href="${pageContext.request.contextPath}/cart"> <!-- Open shopping cart -->
                🛒 Cart <span id="cartCount">0</span> <!-- Icon and badge updated by JS -->
            </a>
        </div>
    </div>

    <!-- SEARCH FORM -->
    <div class="search-box"> <!-- Keyword search -->
        <form action="${pageContext.request.contextPath}/menu" method="get"> <!-- GET keeps URL shareable -->
            <input type="hidden" name="action" value="search"> <!-- Tells MenuServlet to search -->
            <input type="text" name="keyword" placeholder="Search food..." value="<c:out value='${searchKeyword}' default='' />"> <!-- Search box, keeps last keyword -->
            <button type="submit" class="btn">Search</button> <!-- Submit search -->
            <a href="${pageContext.request.contextPath}/menu" class="btn">Reset</a> <!-- Clear filters -->
        </form>
    </div>

    <!-- CATEGORY FILTER FORM -->
    <div class="search-box"> <!-- Filter by cuisine category -->
        <form action="${pageContext.request.contextPath}/menu" method="get"> <!-- GET filter -->
            <input type="hidden" name="action" value="category"> <!-- Tells MenuServlet to filter -->
            <select name="category"> <!-- Dropdown of categories -->
                <option value="">All Categories</option> <!-- Show everything -->
                <option value="Nepali" ${selectedCategory == 'Nepali' ? 'selected' : ''}>Nepali</option> <!-- Nepali dishes -->
                <option value="Chinese" ${selectedCategory == 'Chinese' ? 'selected' : ''}>Chinese</option> <!-- Chinese dishes -->
                <option value="Indian" ${selectedCategory == 'Indian' ? 'selected' : ''}>Indian</option> <!-- Indian dishes -->
                <option value="Italian" ${selectedCategory == 'Italian' ? 'selected' : ''}>Italian</option> <!-- Italian dishes -->
                <option value="FastFood" ${selectedCategory == 'FastFood' ? 'selected' : ''}>Fast Food</option> <!-- Fast food -->
                <option value="Beverages" ${selectedCategory == 'Beverages' ? 'selected' : ''}>Beverages</option> <!-- Drinks -->
            </select>
            <button type="submit" class="btn">Filter</button> <!-- Apply category -->
        </form>
    </div>

    <!-- MENU ITEMS GRID -->
    <div class="menu-grid"> <!-- Responsive card grid -->
        <c:forEach var="item" items="${menuItems}"> <%-- One card per menu row --%>
            <div class="menu-card"> <!-- Single dish card -->
                <!-- Food image -->
                <c:choose> <%-- Show photo or placeholder --%>
                    <c:when test="${not empty item.image}"> <%-- Has uploaded image --%>
                        <img src="${pageContext.request.contextPath}/uploads/${item.image}" alt="${item.name}" class="food-image"> <!-- Dish photo -->
                    </c:when>
                    <c:otherwise> <%-- No image on file --%>
                        <div class="food-image food-image-placeholder">&#9733;</div> <!-- Star placeholder -->
                    </c:otherwise>
                </c:choose>

                <!-- Food details -->
                <div class="menu-info"> <!-- Name, category, price, status -->
                    <h3>${item.name}</h3> <!-- Dish name -->
                    <p class="category">${item.category}</p> <!-- Cuisine type -->
                    <p class="price">Rs. ${item.price}</p> <!-- Price in rupees -->
                    <p class="status">${item.availability}</p> <!-- available or not -->
                </div>

                <!-- Add to cart button -->
                <div class="menu-action"> <!-- Add button or sold-out -->
                    <c:if test="${item.availability == 'available'}"> <%-- Only if in stock --%>
                        <button class="add-btn"
                                data-id="${item.menuId}"
                                data-name="${item.name}"
                                data-price="${item.price}"
                                onclick="addToCart(this)">+</button> <!-- AJAX add to cart -->
                    </c:if>
                    <c:if test="${item.availability != 'available'}"> <%-- Out of stock --%>
                        <button class="add-btn disabled" disabled>OUT</button> <!-- Disabled when unavailable -->
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>

</div>

<div id="cartToast" class="cart-toast" role="status" aria-live="polite">Item added to cart</div> <!-- Brief confirmation toast -->

<!-- JavaScript for adding to cart without page refresh -->
<script>
    // Variable to store current cart count
    let cartItemCount = 0; // Running total shown in the header

    function showCartToast() { // Fade in the “added to cart” message
        var toast = document.getElementById("cartToast"); // Toast element
        toast.classList.add("show"); // Make it visible
        clearTimeout(toast.hideTimer); // Cancel any pending hide
        toast.hideTimer = setTimeout(function() { // Auto-hide after a few seconds
            toast.classList.remove("show"); // Hide again
        }, 2500); // 2.5 second display
    }

    // Function to add item to cart (no page refresh)
    function addToCart(button) { // Called when user taps +
        // Get item details from button attributes
        let menuId = button.getAttribute("data-id"); // Menu item id
        let name = button.getAttribute("data-name"); // Display name
        let price = button.getAttribute("data-price"); // Unit price

        // Create form data to send
        let formData = new URLSearchParams(); // Body for POST
        formData.append("action", "add"); // CartServlet action
        formData.append("menuId", menuId); // Which dish
        formData.append("name", name); // Name for cart line
        formData.append("price", price); // Price for cart line
        formData.append("quantity", "1"); // One at a time from menu

        // Send to cart servlet using fetch (page does not refresh)
        fetch("${pageContext.request.contextPath}/cart", { // POST to /cart
            method: "POST", // Create/update cart
            headers: {
                "Content-Type": "application/x-www-form-urlencoded" // Form-style body
            },
            body: formData // action, menuId, name, price, quantity
        })
            .then(function(response) { // After server responds
                if (response.ok) { // Add succeeded
                    cartItemCount = cartItemCount + 1; // Bump local counter
                    document.getElementById("cartCount").innerText = cartItemCount; // Update badge
                    showCartToast(); // Tell the user it worked
                }
            })
            .catch(function(error) { // Network or server error
                console.log("Error:", error); // Log for debugging
            });
    }

    // Load current cart count when page first loads
    window.onload = function() { // Sync badge with session cart
        fetch("${pageContext.request.contextPath}/cart?action=count", { // GET item count
            method: "GET" // Read-only
        })
            .then(function(response) { // Parse JSON body
                return response.json(); // { count: number }
            })
            .then(function(data) { // { count: N }
                cartItemCount = data.count; // Store server count
                document.getElementById("cartCount").innerText = cartItemCount; // Show on page
            })
            .catch(function(error) { // Could not reach cart servlet
                console.log("Error getting cart count:", error);
            });
    }
</script>

</body>
</html>
