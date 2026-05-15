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

        <img src="https://nepalhouse.com.np/uploads/contents/nepal-house-about.jpg">

    </div>

</section>


<%@ include file="includes/footer.jsp" %>