// ============================================
// MAIN.JS - Shared JavaScript for all pages
// ============================================

// This function adds item to cart using session
// It sends data to CartServlet using AJAX
function addToCart(menuId, name, price) {

    // Show loading feedback (optional)
    console.log("Adding to cart: " + name);

    // Create data to send to server
    let formData = new URLSearchParams();
    formData.append("action", "add");
    formData.append("menuId", menuId);
    formData.append("name", name);
    formData.append("price", price);
    formData.append("quantity", 1);

    // Send POST request to CartServlet
    fetch(getContextPath() + "/cart", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: formData.toString()
    })
        .then(function(response) {
            if (response.ok) {
                // If successful, update cart count
                updateCartCount();
                alert(name + " added to cart!");
            } else {
                alert("Failed to add to cart. Please try again.");
            }
        })
        .catch(function(error) {
            console.error("Error:", error);
            alert("Something went wrong!");
        });
}

// This function gets the current number of items in cart
// It calls CartServlet to get count from session
function updateCartCount() {

    fetch(getContextPath() + "/cart?action=getCount")
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            // Update the cart count display
            var cartSpan = document.getElementById("cartCount");
            if (cartSpan) {
                cartSpan.innerText = data.count || 0;
            }
        })
        .catch(function(error) {
            console.error("Error getting cart count:", error);
        });
}

// This function redirects to cart page
function goToCart() {
    window.location.href = getContextPath() + "/cart";
}

// Helper function to get base URL of the website
// Example: if website is http://localhost:8080/restaurant, this returns "/restaurant"
function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}

// Alternative simple way to get context path
function getContextPathSimple() {
    return "${pageContext.request.contextPath}"; // This works only in JSP, not in JS file
}

// When page loads, update cart count
document.addEventListener("DOMContentLoaded", function() {
    updateCartCount();
});