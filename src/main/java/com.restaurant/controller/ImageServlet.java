package com.restaurant.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

// This servlet serves images from the project's images folder
@WebServlet("/uploads/*")
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        // Get the filename from URL
        // Example: /uploads/steam-momo.jpg -> filename = steam-momo.jpg
        String filename = request.getPathInfo();
        if (filename == null || filename.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Remove the leading slash
        filename = filename.substring(1);

        // Get the path to images folder inside the project
        // This works on any computer because it uses the project location
        String uploadPath = getServletContext().getRealPath("/image");
        File file = new File(uploadPath, filename);

        // Check if file exists
        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Security check - make sure file is inside images folder
        if (!file.getCanonicalPath().startsWith(new File(uploadPath).getCanonicalPath())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Set the content type (image/jpeg, image/png, etc.)
        String contentType = Files.probeContentType(file.toPath());
        if (contentType != null) {
            response.setContentType(contentType);
        }

        // Set the file length
        response.setContentLengthLong(file.length());

        // Send the image to browser
        try (OutputStream out = response.getOutputStream()) {
            Files.copy(file.toPath(), out);
        }
    }
}