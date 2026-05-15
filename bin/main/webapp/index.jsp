<%@ include file="includes/header.jsp" %>
<%@ include file="includes/navbar.jsp" %>
<style>
    /* ================= RESERVATION ================= */

    .reservation{
        min-height:100vh;

        display:grid;
        grid-template-columns:repeat(auto-fit,minmax(320px,1fr));

        align-items:center;

        padding:120px 8% 80px;
        gap:60px;
    }

    .reservation-text h1{
        font-size:88px;
        line-height:1;
        font-family:'Cormorant Garamond',serif;
        margin-bottom:25px;
        color:#2d2d2d;
    }

    .reservation-text p{
        font-size:18px;
        line-height:1.9;
        color:#6d6d6d;
        margin-bottom:40px;
        max-width:600px;
    }

    .reservation-buttons{
        display:flex;
        gap:20px;
        flex-wrap:wrap;
    }

    .outline-btn{
        padding:14px 28px;
        border:1px solid #b58b65;
        border-radius:40px;
        color:#b58b65;
        transition:0.3s;
    }

    .outline-btn:hover{
        background:#b58b65;
        color:white;
    }

    .reservation-image img{
        width:100%;
        border-radius:30px;
        object-fit:cover;
        box-shadow:0 20px 60px rgba(0,0,0,0.1);
    }
    /* ================= SECTION ================= */

    .section{
        padding:110px 8%;
    }

    .section-title{
        text-align:center;
        margin-bottom:70px;
    }

    .section-title h2{
        font-size:60px;
        font-family:'Cormorant Garamond',serif;
        margin-bottom:20px;
        color:#2d2d2d;
    }

    .section-title p{
        color:#7a7a7a;
        max-width:700px;
        margin:auto;
        line-height:1.8;
    }
    /* ================= FEATURES ================= */

    .features{
        display:grid;
        grid-template-columns:repeat(auto-fit,minmax(250px,1fr));
        gap:30px;
    }

    .feature-card{
        background:white;
        padding:40px 30px;
        border-radius:25px;
        text-align:center;
        box-shadow:0 10px 30px rgba(0,0,0,0.05);

        transition:0.4s;
    }

    .feature-card:hover{
        transform:translateY(-10px);
    }

    .feature-card i{
        font-size:45px;
        color:#b58b65;
        margin-bottom:25px;
    }

    .feature-card h3{
        font-size:24px;
        margin-bottom:15px;
    }

    .feature-card p{
        color:#777;
        line-height:1.8;
    }
    /* ================= MENU ================= */

    .menu-grid{
        display:grid;
        grid-template-columns:repeat(auto-fit,minmax(300px,1fr));
        gap:35px;
    }

    .menu-card{
        background:white;
        border-radius:30px;
        overflow:hidden;
        box-shadow:0 15px 40px rgba(0,0,0,0.06);

        transition:0.4s;
    }

    .menu-card:hover{
        transform:translateY(-10px);
    }

    .menu-card img{
        width:100%;
        height:280px;
        object-fit:cover;
    }

    .menu-content{
        padding:30px;
    }

    .menu-content h3{
        font-size:28px;
        margin-bottom:12px;
    }

    .menu-content p{
        color:#777;
        line-height:1.8;
    }

    .price{
        display:inline-block;
        margin-top:18px;
        color:#b58b65;
        font-size:24px;
        font-weight:600;
    }
    /* ================= STORY SECTION ================= */

    .story{
        display:grid;
        grid-template-columns:repeat(auto-fit,minmax(320px,1fr));
        gap:70px;
        align-items:center;
    }

    .story img{
        width:100%;
        border-radius:30px;
    }

    .story-text h2{
        font-size:60px;
        font-family:'Cormorant Garamond',serif;
        margin-bottom:25px;
    }

    .story-text p{
        line-height:1.9;
        color:#6f6f6f;
        margin-bottom:25px;
    }

    /* ================= TESTIMONIALS ================= */

    .testimonials{
        display:grid;
        grid-template-columns:repeat(auto-fit,minmax(320px,1fr));
        gap:30px;
    }

    .testimonial{
        background:white;
        padding:40px;
        border-radius:25px;
        box-shadow:0 10px 30px rgba(0,0,0,0.05);
    }

    .testimonial p{
        line-height:1.9;
        color:#666;
        margin-bottom:20px;
    }

    .testimonial h4{
        color:#b58b65;
    }

    /* ================= RESERVATION BANNER ================= */

    .reservation-banner{
        background:#b58b65;
        padding:90px 8%;
        text-align:center;
        color:white;
        border-radius:40px;
    }

    .reservation-banner h2{
        font-size:60px;
        font-family:'Cormorant Garamond',serif;
        margin-bottom:20px;
    }

    .reservation-banner p{
        max-width:700px;
        margin:auto;
        line-height:1.8;
        margin-bottom:35px;
    }

    .reservation-banner .btn{
        background:white;
        color:#b58b65;
    }
</style>
<section class="reservation">

    <div class="reservation-text">

        <h1>
            Elegant Dining <br>
            Inspired by <br>
            Himalayan Luxury
        </h1>

        <p>
            Michelin-Star Restaurant delivers an extraordinary fine
            dining experience through exquisite cuisine, refined hospitality,
            and timeless Himalayan elegance.
        </p>

        <div class="reservation-buttons">

            <a href="${pageContext.request.contextPath}/views/customer/menu.jsp" class="btn">
                Explore Menu
            </a>

            <a href="${pageContext.request.contextPath}/login.jsp" class="outline-btn">
                Reserve Now
            </a>

        </div>

    </div>

    <div class="reservation-image">

        <img src="https://nepalhouse.com.np/uploads/contents/nepal-house-about.jpg" alt="reservation-image">

    </div>

</section>
<!-- ================= FEATURES ================= -->

<section class="section">

    <div class="section-title">

        <h2>Why Guests Love Us</h2>

        <p>
            A blend of culinary artistry, exceptional hospitality,
            and timeless atmosphere.
        </p>

    </div>

    <div class="features">

        <div class="feature-card">
            <i class="fas fa-utensils"></i>
            <h3>Fine Cuisine</h3>
            <p>Prepared with fresh premium ingredients and artistic presentation.</p>
        </div>

        <div class="feature-card">
            <i class="fas fa-wine-glass"></i>
            <h3>Premium Wines</h3>
            <p>Exclusive wine collections curated by our expert sommeliers.</p>
        </div>

        <div class="feature-card">
            <i class="fas fa-music"></i>
            <h3>Luxury Ambience</h3>
            <p>Elegant interiors with relaxing music and candlelight atmosphere.</p>
        </div>

    </div>

</section>
<!-- ================= MENU ================= -->

<section class="section">

    <div class="section-title">

        <h2>Signature Menu</h2>

        <p>
            Discover our chef’s carefully crafted specialties.
        </p>

    </div>

    <div class="menu-grid">

        <div class="menu-card">

            <img src="https://images.unsplash.com/photo-1544025162-d76694265947?q=80&w=1974&auto=format&fit=crop">

            <div class="menu-content">

                <h3>Grilled Steak</h3>

                <p>
                    Tender premium steak served with roasted vegetables
                    and creamy pepper sauce.
                </p>

                <span class="price">$34.99</span>

            </div>

        </div>

        <div class="menu-card">

            <img src="https://images.unsplash.com/photo-1559847844-d721426d6edc?q=80&w=1974&auto=format&fit=crop">

            <div class="menu-content">

                <h3>Truffle Pasta</h3>

                <p>
                    Handmade pasta tossed with creamy truffle sauce
                    and parmesan cheese.
                </p>

                <span class="price">$24.99</span>

            </div>

        </div>

        <div class="menu-card">

            <img src="https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=2080&auto=format&fit=crop">

            <div class="menu-content">

                <h3>Fresh Salmon</h3>

                <p>
                    Oven baked salmon glazed with herbs
                    and lemon butter.
                </p>

                <span class="price">$29.99</span>

            </div>

        </div>

    </div>

</section>

<!-- ================= STORY ================= -->

<section class="section">

    <div class="story">

        <img src="https://images.unsplash.com/photo-1559339352-11d035aa65de?q=80&w=2070&auto=format&fit=crop">

        <div class="story-text">

            <h2>
                Our Culinary Story
            </h2>

            <p>
                Since 1998, Maison Blanche has delivered refined dining
                experiences inspired by European elegance and modern cuisine.
            </p>

            <p>
                Every dish is prepared with dedication, passion,
                and creativity by our award-winning chefs.
            </p>

            <a href="#" class="btn">
                Read More
            </a>

        </div>

    </div>

</section>

<!-- ================= TESTIMONIALS ================= -->

<section class="section">

    <div class="section-title">

        <h2>Guest Experiences</h2>

        <p>
            Hear what our valued guests say about us.
        </p>

    </div>

    <div class="testimonials">

        <div class="testimonial">

            <p>
                “Absolutely the best fine dining experience I’ve had.
                Elegant interiors and incredible food.”
            </p>

            <h4>— Emma Watson</h4>

        </div>

        <div class="testimonial">

            <p>
                “The ambience feels luxurious and relaxing.
                Every dish tasted extraordinary.”
            </p>

            <h4>— David Miller</h4>

        </div>

        <div class="testimonial">

            <p>
                “Perfect place for celebrations and romantic dinners.
                Highly recommended.”
            </p>

            <h4>— Sophia Clark</h4>

        </div>

    </div>

</section>

<!-- ================= RESERVATION ================= -->

<section class="section">

    <div class="reservation-banner">

        <h2>
            Reserve Your Table Today
        </h2>

        <p>
            Experience unforgettable dining moments with premium cuisine,
            exceptional hospitality, and elegant atmosphere.
        </p>

        <a href="#" class="btn">
            Make Reservation
        </a>

    </div>

</section>

<%@ include file="includes/footer.jsp" %>