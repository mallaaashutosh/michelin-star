<%--
  Site footer fragment — contact, hours, social links, and copyright.
  Closes </body> and </html> opened by header.jsp; include after main page content.
--%>

<footer id="contact">

    <div class="footer-grid"> <!-- Four-column grid for footer sections -->

        <div class="footer-box"> <!-- Brand blurb and social icons -->
            <h3>Michelin-Star</h3>
            <p>Fine dining inspired by timeless European elegance.</p>

            <div class="social-icons"> <!-- Links to social profiles -->
                <a href="https://www.facebook.com/" target="_blank">
                    <img src="${pageContext.request.contextPath}/image/facebook.jpg"
                         alt="Facebook" class="social-logo">
                </a>
                <a href="https://www.instagram.com/" target="_blank">
                    <img src="${pageContext.request.contextPath}/image/instagram.jpg"
                         alt="Instagram" class="social-logo">
                </a>
                <a href="https://x.com/" target="_blank">
                    <img src="${pageContext.request.contextPath}/image/twitter.png"
                         alt="Twitter" class="social-logo">
                </a>
            </div>
        </div>

        <div class="footer-box"> <!-- Quick navigation links -->
            <h3>Quick Links</h3>
            <a href="#">Home</a>
            <a href="#">Menu</a>
            <a href="#">Gallery</a>
        </div>

        <div class="footer-box"> <!-- Opening hours -->
            <h3>Opening Hours</h3>
            <p>Mon - Fri : 10am - 11pm</p>
            <p>Saturday : 9am - 12am</p>
            <p>Sunday : Closed</p>
        </div>

        <div class="footer-box"> <!-- Address and contact details -->
            <h3>Contact</h3>
            <p>Mahendrapool, Pokhara</p>
            <p>+977 9702654822</p>
            <p>michelinstar@gmail.com</p>
        </div>

    </div>

    <div class="copyright"> <!-- Copyright line -->
        &copy; 2026 Maison Blanche Restaurant. All Rights Reserved.
    </div>

</footer>

<!-- Closes <body> and <html> opened in header.jsp -->
</body>
</html>
