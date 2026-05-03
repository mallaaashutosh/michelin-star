<%@ include file="includes/header.jsp" %>
<%@ include file="includes/navbar.jsp" %>

<section class="hero">
    <img src="${pageContext.request.contextPath}/image/logo.png"
         alt="Michelin-Star Logo" class="hero-logo">

    <h1 class="hero-title">Welcome to Michelin-Star</h1>

    <p class="hero-text">
        Enjoy exceptional food where every dish is carefully prepared
        with passion, precision, and dedication by our award-winning chefs,
        using the finest ingredients to deliver a memorable fine dining experience.
    </p>

    <a href="${pageContext.request.contextPath}/views/customer/reservation.jsp" class="btn">
        Reserve Your Table
    </a>
</section>

<section class="experience">
    <h2>Our Signature Experience</h2>

    <div class="cards">

        <!-- Card 1 -->
        <div class="card">
            <div class="icon">
                <svg viewBox="0 0 24 24">
                    <path d="M7 2v20M11 2v8M11 12v10M7 6h4"/>
                    <path d="M16 2c0 6-2 6-2 10v10"/>
                </svg>
            </div>
            <h3>Table Service</h3>
            <p>Enjoy exceptional food served with care by our skilled chefs.</p>
        </div>

        <!-- Card 2 -->
        <div class="card">
            <div class="icon">
                <svg viewBox="0 0 24 24">
                    <rect x="3" y="5" width="18" height="16" rx="2"/>
                    <line x1="8" y1="3" x2="8" y2="7"/>
                    <line x1="16" y1="3" x2="16" y2="7"/>
                    <line x1="3" y1="9" x2="21" y2="9"/>
                </svg>
            </div>
            <h3>Reservation</h3>
            <p>Reserve your table to enjoy a premium fine dining experience.</p>
        </div>

        <!-- Card 3 -->
        <div class="card">
            <div class="icon">
                <svg viewBox="0 0 24 24">
                    <circle cx="12" cy="12" r="8"/>
                    <circle cx="12" cy="12" r="3"/>
                </svg>
            </div>
            <h3>Fine Dining</h3>
            <p>Relax in an elegant atmosphere with world-class cuisine.</p>
        </div>

    </div>
</section>

<%@ include file="includes/footer.jsp" %>